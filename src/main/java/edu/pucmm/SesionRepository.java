package edu.pucmm;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class SesionRepository {

    // Obtenemos la base de datos de la conexión centralizada
    private static final MongoDatabase database = MongoDBConnection.getDatabase();
    // Definimos la colección donde se guardarán las sesiones, por ejemplo "sesiones"
    private static final MongoCollection<Document> sesionesCollection = database.getCollection("sesiones");

    // Método para guardar una sesión en MongoDB
    public static void guardarSesion(Sesion sesion) {
        // Convertir el objeto Sesion a un Document de MongoDB
        Document doc = new Document("token", sesion.getToken())
                .append("usuario", new Document("id", sesion.getUsuario().getId().toString())
                        .append("username", sesion.getUsuario().getUsername())
                        .append("nombre", sesion.getUsuario().getNombre())
                        .append("admin", sesion.getUsuario().getAdmin())
                        .append("activo", sesion.getUsuario().getActivo()));
        // Insertamos el documento en la colección
        sesionesCollection.insertOne(doc);
    }

    // Método para buscar una sesión en MongoDB usando el token
    public static Sesion buscarSesion(String token) {
        Document doc = sesionesCollection.find(new Document("token", token)).first();
        if (doc != null) {
            // Extraer datos del usuario (aquí se asume que se almacena anidado en "usuario")
            Document usuarioDoc = (Document) doc.get("usuario");
            Usuario usuario = new Usuario();
            usuario.setId(new org.bson.types.ObjectId(usuarioDoc.getString("id")));
            usuario.setUsername(usuarioDoc.getString("username"));
            usuario.setNombre(usuarioDoc.getString("nombre"));
            usuario.setAdmin(usuarioDoc.getBoolean("admin"));
            usuario.setActivo(usuarioDoc.getBoolean("activo"));

            // Retornamos la sesión creada a partir del token y el usuario
            return new Sesion(token, usuario);
        }
        return null;
    }

    // Opcional: Método para eliminar una sesión (por ejemplo, al cerrar sesión)
    public static void eliminarSesion(String token) {
        sesionesCollection.deleteOne(new Document("token", token));
    }
}
