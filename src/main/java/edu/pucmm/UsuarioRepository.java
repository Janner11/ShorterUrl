package edu.pucmm;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import edu.pucmm.Usuario;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository {

    private static final MongoDatabase database = MongoDBConnection.getDatabase();
    private static final MongoCollection<Document> usuariosCollection = database.getCollection("usuarios");

    // Método para buscar un usuario por username
    public static Usuario buscarUsuario(String username) {
        Document usuarioDoc = usuariosCollection.find(new Document("username", username)).first();
        if (usuarioDoc != null) {
            return documentToUsuario(usuarioDoc);
        }
        return null;
    }

    // Método para guardar un usuario en la base de datos
    public static void guardarUsuario(Usuario usuario) {
        Document usuarioDoc = new Document("_id", new ObjectId())
                .append("username", usuario.getUsername())
                .append("nombre", usuario.getNombre())
                .append("password", usuario.getPassword())
                .append("admin", usuario.getAdmin())
                .append("activo", usuario.getActivo());

        usuariosCollection.insertOne(usuarioDoc);
    }

    public static void actualizarUsuario(Usuario usuario) {
        // Se utiliza "username" como identificador único del usuario.
        Document filtro = new Document("username", usuario.getUsername());

        // Se actualizan los campos necesarios usando el operador $set.
        Document actualizacion = new Document("$set", new Document("admin", usuario.getAdmin())
                .append("nombre", usuario.getNombre())
                .append("password", usuario.getPassword())
                .append("activo", usuario.getActivo()));
        usuariosCollection.updateOne(filtro, actualizacion);
    }


    // Método para convertir un Document de MongoDB a un objeto Usuario
    private static Usuario documentToUsuario(Document doc) {
        Usuario usuario = new Usuario();
        usuario.setId(doc.getObjectId("_id"));
        usuario.setUsername(doc.getString("username"));
        usuario.setNombre(doc.getString("nombre"));
        usuario.setPassword(doc.getString("password"));
        usuario.setAdmin(doc.getBoolean("admin"));
        usuario.setActivo(doc.getBoolean("activo"));
        return usuario;
    }
    public static List<Usuario> obtenerUsuariosNoAdmin() {
        List<Usuario> usuarios = new ArrayList<>();
        // Filtramos para obtener los usuarios donde "admin" es false
        MongoCollection<Document> collection = MongoDBConnection.getDatabase().getCollection("usuarios");
        for (Document doc : collection.find(Filters.eq("admin", false))) {
            usuarios.add(documentToUsuario(doc));
        }
        return usuarios;
    }
}
