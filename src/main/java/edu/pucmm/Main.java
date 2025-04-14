package edu.pucmm;

import io.javalin.Javalin;
import com.google.gson.Gson;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Map;
import org.bson.types.ObjectId;

public class Main {

    private static Gson gson = new Gson();

    public static void main(String[] args) {

        System.out.println("Iniciando servidor...");
        var app = Javalin.create(config -> {
            config.staticFiles.add("/publico");
        }).start(7000);

        app.before(ctx -> {
            ctx.header("Access-Control-Allow-Origin", "*");
            ctx.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            ctx.header("Access-Control-Allow-Headers", "Content-Type, Authorization");
            ctx.header("Access-Control-Allow-Credentials", "true");
            ctx.header("Access-Control-Expose-Headers", "Authorization");
            if (ctx.method().equals("OPTIONS")) {
                ctx.status(204);
            }
        });
        // Usuario admin por defecto
        Usuario admin = UsuarioRepository.buscarUsuario("admin");
        if (admin == null) {
            admin = new Usuario("admin", "Administrador", "admin", true, true);
            UsuarioRepository.guardarUsuario(admin);
        }

        app.before(ctx -> {
            // Si el header "Authorization" está presente, usamos el token para validar el usuario
            String authHeader = ctx.header("Authorization");
            if (authHeader != null && !authHeader.isEmpty()) {
                String token = authHeader.replace("Bearer ", "");
                Usuario usuarioToken = JWTUtil.validarToken(token);
                if (usuarioToken != null) {
                    // Guardamos el nombre de usuario en el contexto para que las rutas protegidas lo usen
                    ctx.attribute("user", usuarioToken.getUsername());
                }
            }
        });

        app.get("/", ctx -> ctx.redirect("/index.html"));

        // Registro
        app.post("/registro", ctx -> {
            String username = ctx.formParam("username");
            String nombre = ctx.formParam("nombre");
            String password = ctx.formParam("password");
            boolean esAdmin = Boolean.parseBoolean(ctx.formParam("admin"));
            boolean activo = Boolean.parseBoolean(ctx.formParam("activo"));

            Usuario usuarioExistente = UsuarioRepository.buscarUsuario(username);
            if (usuarioExistente != null) {
                ctx.status(400).result("El usuario ya existe.");
                return;
            }
            Usuario nuevo = new Usuario(username, nombre, password, esAdmin, activo);
            UsuarioRepository.guardarUsuario(nuevo);
            ctx.status(201).result("Usuario registrado exitosamente.");
        });

        // Login con JWT y sesión
        app.post("/login", ctx -> {
            String username = ctx.formParam("username");
            String password = ctx.formParam("password");

            Usuario usuarioLogin = UsuarioRepository.buscarUsuario(username);
            if (usuarioLogin != null && usuarioLogin.getPassword().equals(password)) {
                ctx.sessionAttribute("USUARIO", usuarioLogin);
                String token = JWTUtil.generarToken(usuarioLogin);
                ctx.json(Map.of("token", token));
            } else {
                ctx.status(401).result("Usuario o contraseña incorrectos");
            }
        });

        // Logout
        app.get("/logout", ctx -> {
            ctx.sessionAttribute("USUARIO", null);
            ctx.redirect("/index.html");
        });

        // Obtener info del usuario logueado
        app.get("/getUserInfo", ctx -> {
            Usuario usuarioSesion = obtenerUsuarioSesionOToken(ctx);
            if (usuarioSesion == null) {
                ctx.status(401).json(Map.of("error", "Not logged in"));
            } else {
                ctx.json(usuarioSesion);
            }
        });

        // Listar usuarios no-admin para el formulario de promoción
        app.get("/listUsuarios", ctx -> {
            Usuario usuarioSesion = validarUsuarioAdmin(ctx);
            if (usuarioSesion == null) return;
            ctx.json(UsuarioRepository.obtenerUsuariosNoAdmin());
        });


        // Acortar URL
        app.post("/acortar", ctx -> {
            String urlOriginal = ctx.formParam("urlOriginal");
            Usuario usuario = ctx.sessionAttribute("USUARIO");
            String usuarioId = (usuario != null) ? usuario.getId().toString() : "No Registrado";

            String urlCorta = generarUrlCorta(urlOriginal);
            UrlAcortada urlAcortada = new UrlAcortada(urlOriginal, urlCorta, usuarioId, new Date());
            urlAcortada.setAccesos(0);
            UrlRepository.guardarUrl(urlAcortada);

            ctx.status(201).result("URL acortada: " + urlCorta);
        });

        // Redirección: Capturamos el dominio usando ctx.host()
        app.get("/go/{codigo}", ctx -> {
            String codigo = ctx.pathParam("codigo");
            String urlCortaCompleta = "http://localhost:7000/go/" + codigo;
            UrlAcortada urlAcortada = UrlRepository.buscarPorUrlCorta(urlCortaCompleta);

            if (urlAcortada != null) {
                String dominio = ctx.host(); // Obtenemos el dominio del cliente (host)
                Acceso acceso = new Acceso(new Date(), ctx.ip(), ctx.userAgent(), "Desconocido", dominio);
                UrlRepository.registrarAcceso(urlCortaCompleta, acceso);
                ctx.redirect(urlAcortada.getUrlOriginal());
            } else {
                ctx.status(404).result("URL no encontrada.");
            }
        });

        // Promover usuario a admin
        app.post("/promover", ctx -> {
            Usuario usuarioSesion = ctx.sessionAttribute("USUARIO");
            if (usuarioSesion == null || !usuarioSesion.getAdmin()) {
                ctx.status(403).result("No tienes permisos para promover usuarios.");
                return;
            }
            String username = ctx.formParam("username");
            Usuario usuarioPromover = UsuarioRepository.buscarUsuario(username);
            if (usuarioPromover != null) {
                usuarioPromover.setAdmin(true);
                UsuarioRepository.actualizarUsuario(usuarioPromover);
                ctx.status(200).result("Usuario promovido a administrador exitosamente.");
            } else {
                ctx.status(404).result("Usuario no encontrado.");
            }
        });

        // Vista Previa vía Microlink
        app.get("/preview", ctx -> {
            String url = ctx.queryParam("url");
            if (url == null || url.isEmpty()) {
                ctx.status(400).result("Falta el parámetro url");
                return;
            }
            try {
                HttpClient client = HttpClient.newHttpClient();
                HttpRequest request = HttpRequest.newBuilder()
                        .uri(URI.create("https://api.microlink.io/?url=" + url))
                        .build();
                HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
                ctx.contentType("application/json");
                ctx.result(response.body());
            } catch (Exception e) {
                ctx.status(500).result("Error obteniendo vista previa");
            }
        });

        // Listado de URLs para administración (solo admin)
        app.get("/listUrls", ctx -> {
            Usuario usuarioSesion = validarUsuarioAdmin(ctx);
            if (usuarioSesion == null) return;
            ctx.json(UrlRepository.listarUrls());
        });

        // Eliminar enlace (solo admin)
        app.post("/eliminar", ctx -> {
            Usuario usuarioSesion = validarUsuarioAdmin(ctx);
            if (usuarioSesion == null) return;
            String id = ctx.formParam("id");
            if (id == null || id.isEmpty()) {
                ctx.status(400).result("Falta el id del enlace");
                return;
            }
            if (!ObjectId.isValid(id)) {
                ctx.status(400).result("El ID no es válido. Debe ser un ObjectId de 24 hex.");
                return;
            }
            boolean eliminado = UrlRepository.eliminarUrl(id);
            if (eliminado) {
                ctx.status(200).result("Enlace eliminado exitosamente.");
            } else {
                ctx.status(500).result("Error eliminando el enlace.");
            }
        });

        // Estadísticas para el dashboard
        app.get("/dashboardStats", ctx -> {
            DashboardStats stats = StatsRepository.obtenerEstadisticasGlobales();
            ctx.json(stats);
        });

        // Estadísticas para el dashboard filtradas por id de usuario
        app.get("/userDashboardStats", ctx -> {
            // Intentamos obtener el ID del usuario desde el atributo "userId"
            String userId = ctx.attribute("userId");
            if (userId == null) {
                // Si no existe el atributo, se busca en la sesión
                Usuario usuarioSesion = ctx.sessionAttribute("USUARIO");
                if (usuarioSesion != null) {
                    userId = usuarioSesion.getId().toString(); // Usamos el ID del usuario
                    ctx.attribute("userId", userId);
                } else {
                    ctx.status(401).result("No autorizado");
                    return;
                }
            }

            DashboardStats stats = StatsRepository.obtenerEstadisticasPorUsuario(userId);
            ctx.json(stats);
        });

        // Listado de URLs del usuario filtradas por id
        app.get("/listUserUrls", ctx -> {
            String userId = ctx.attribute("userId");
            if (userId == null) {
                Usuario usuarioSesion = ctx.sessionAttribute("USUARIO");
                if (usuarioSesion != null) {
                    userId = usuarioSesion.getId().toString();
                    ctx.attribute("userId", userId);
                } else {
                    ctx.status(401).result("No autorizado");
                    return;
                }
            }

            ctx.json(UrlRepository.obtenerUrlsPorUsuarioComoDocumentos(userId));
        });


        app.post("/eliminarUsuario", ctx -> {
            Usuario usuarioSesion = obtenerUsuarioSesionOToken(ctx);
            if (usuarioSesion == null) {
                ctx.status(401).result("No estás autenticado.");
                return;
            }

            String id = ctx.formParam("id");
            if (id == null || id.isEmpty()) {
                ctx.status(400).result("Falta el id del enlace.");
                return;
            }
            if (!ObjectId.isValid(id)) {
                ctx.status(400).result("El ID no es válido. Debe ser un ObjectId de 24 hex.");
                return;
            }

            boolean eliminado = UrlRepository.eliminarUrl(id);
            if (eliminado) {
                ctx.status(200).result("Enlace eliminado exitosamente.");
            } else {
                ctx.status(500).result("Error eliminando el enlace.");
            }
        });

    }

    private static String generarUrlCorta(String urlOriginal) {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String urlCorta;
        Random random = new Random();
        do {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 6; i++) {
                sb.append(caracteres.charAt(random.nextInt(caracteres.length())));
            }
            urlCorta = sb.toString();
        } while (UrlRepository.buscarPorUrlCorta(urlCorta) != null);
        return "http://localhost:7000/go/" + urlCorta;
    }

    private static Usuario obtenerUsuarioSesionOToken(io.javalin.http.Context ctx) {
        Usuario usuarioSesion = ctx.sessionAttribute("USUARIO");
        if (usuarioSesion == null) {
            String token = ctx.header("Authorization");
            if (token != null && !token.isEmpty()) {
                token = token.replace("Bearer ", "");
                usuarioSesion = JWTUtil.validarToken(token);
            }
        }
        return usuarioSesion;
    }


    private static Usuario validarUsuario(io.javalin.http.Context ctx) {
        Usuario usuarioSesion = obtenerUsuarioSesionOToken(ctx);
        if (usuarioSesion == null) {
            ctx.status(401).result("No estás autenticado.");
            return null;
        }
        return usuarioSesion;
    }

    private static Usuario validarUsuarioAdmin(io.javalin.http.Context ctx) {
        Usuario usuarioSesion = obtenerUsuarioSesionOToken(ctx);
        if (usuarioSesion == null || !usuarioSesion.getAdmin()) {
            ctx.status(403).result("No tienes permisos de administrador.");
            return null;
        }
        return usuarioSesion;
    }
}
