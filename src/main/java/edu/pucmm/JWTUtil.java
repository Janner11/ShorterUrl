package edu.pucmm;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

public class JWTUtil {

    // Clave secreta para firmar el JWT
    private static final String SECRET_KEY = "mi_clave_secreta";

    // Método para generar un token JWT a partir del usuario
    public static String generarToken(Usuario usuario) {
        return JWT.create()
                .withSubject(usuario.getUsername())
                .withClaim("username", usuario.getUsername())
                .withClaim("admin", usuario.getAdmin())
                .withClaim("activo", usuario.getActivo())
                .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))  // 1 hora de expiración
                .sign(Algorithm.HMAC256(SECRET_KEY));
    }

    // Método para verificar y decodificar un token JWT
    public static DecodedJWT verificarToken(String token) {
        return JWT.require(Algorithm.HMAC256(SECRET_KEY))
                .build()
                .verify(token);
    }

    // Método para validar el token y obtener el usuario asociado
    public static Usuario validarToken(String token) {
        try {
            DecodedJWT decodedJWT = verificarToken(token);
            String username = decodedJWT.getClaim("username").asString();
            return UsuarioRepository.buscarUsuario(username);  // Este método buscará el usuario en la base de datos
        } catch (Exception e) {
            return null;  // Si hay un error o el token no es válido, retorna null
        }
    }
}
