package edu.pucmm;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class MongoDBConnection {
    private static MongoClient mongoClient;
    private static MongoDatabase database;

    static {
        // Forzar el uso de TLS 1.2 para el cliente TLS
        System.setProperty("jdk.tls.client.protocols", "TLSv1.2");

        String mongoUri = "mongodb+srv://bijey0511:ssEMYCIiRvfFH9TP@final-project.cnmtthi.mongodb.net/?retryWrites=true&w=majority&appName=final-project";

        try {
            // Crear cliente MongoDB
            mongoClient = MongoClients.create(mongoUri);
            // Obtener la base de datos 'url-cut'
            database = mongoClient.getDatabase("url-cut");
            System.out.println("Conexión exitosa a MongoDB");
        } catch (Exception e) {
            System.err.println("ERROR conectando a MongoDB: " + e.getMessage());
            throw new RuntimeException("ERROR conectando a MongoDB", e);
        }
    }

    // Método para obtener la base de datos
    public static MongoDatabase getDatabase() {
        return database;
    }

    // Opcional: método para cerrar el cliente al finalizar la aplicación
    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
