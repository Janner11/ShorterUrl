package edu.pucmm;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.types.ObjectId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import static edu.pucmm.MongoDBConnection.getDatabase;

public class UrlRepository {

    private static final MongoCollection<Document> collection = getDatabase().getCollection("urls");

    public static void guardarUrl(UrlAcortada url) {
        Document doc = new Document("_id", new ObjectId())
                .append("urlOriginal", url.getUrlOriginal())
                .append("urlCorta", url.getUrlCorta())
                .append("usuarioId", url.getUsuarioId())
                .append("accesos", url.getAccesos())
                .append("historialAccesos", new ArrayList<>())
                .append("fechaCreacion", url.getFechaCreacion());
        collection.insertOne(doc);
    }

    public static UrlAcortada buscarPorUrlCorta(String urlCorta) {
        if (!urlCorta.startsWith("http://localhost:7000/")) {
            urlCorta = "http://localhost:7000/go/" + urlCorta;
        }
        Document doc = collection.find(Filters.eq("urlCorta", urlCorta)).first();
        if (doc != null) {
            UrlAcortada url = new UrlAcortada(
                    doc.getString("urlOriginal"),
                    doc.getString("urlCorta"),
                    doc.getString("usuarioId"),
                    doc.getDate("fechaCreacion")
            );
            url.setId(doc.getObjectId("_id"));
            url.setAccesos(doc.getInteger("accesos", 0));
            List<Document> historialDocs = (List<Document>) doc.get("historialAccesos");
            if (historialDocs != null) {
                List<Acceso> accesos = new ArrayList<>();
                for (Document d : historialDocs) {
                    Acceso acceso = new Acceso();
                    acceso.setFecha(d.getDate("fecha"));
                    acceso.setIp(d.getString("ip"));
                    acceso.setNavegador(d.getString("navegador"));
                    acceso.setSistemaOperativo(d.getString("sistemaOperativo"));
                    acceso.setDominio(d.getString("dominio"));
                    accesos.add(acceso);
                }
                url.setHistorialAccesos(accesos);
            }
            return url;
        }
        return null;
    }

    public static void registrarAcceso(String urlCorta, Acceso acceso) {
        Document accesoDoc = new Document("ip", acceso.getIp())
                .append("fecha", acceso.getFecha())
                .append("navegador", acceso.getNavegador())
                .append("sistemaOperativo", acceso.getSistemaOperativo())
                .append("dominio", acceso.getDominio());
        collection.updateOne(
                Filters.eq("urlCorta", urlCorta),
                Updates.combine(
                        Updates.inc("accesos", 1),
                        Updates.push("historialAccesos", accesoDoc)
                )
        );
    }

    public static boolean eliminarUrl(String id) {
        try {
            ObjectId objId = new ObjectId(id);
            System.out.println("Intentando eliminar documento con _id: " + objId.toHexString());
            var result = collection.deleteOne(Filters.eq("_id", objId));
            System.out.println("Documentos eliminados: " + result.getDeletedCount());
            return result.getDeletedCount() > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public static List<Document> listarUrls() {
        List<Document> urls = new ArrayList<>();
        for (Document doc : collection.find()) {
            if (doc.containsKey("_id") && doc.get("_id") instanceof ObjectId) {
                ObjectId id = doc.getObjectId("_id");
                doc.put("_id", id.toHexString());
            }
            urls.add(doc);
        }
        return urls;
    }
    public static List<Document> obtenerUrlsPorUsuarioComoDocumentos(String usuarioId) {
        List<Document> urls = new ArrayList<>();
        FindIterable<Document> docs = collection.find(Filters.eq("usuarioId", usuarioId));

        for (Document doc : docs) {
            if (doc.containsKey("_id") && doc.get("_id") instanceof ObjectId) {
                ObjectId id = doc.getObjectId("_id");
                doc.put("_id", id.toHexString()); // convierte a string
            }
            urls.add(doc);
        }

        return urls;
    }

    public static UrlAcortada obtenerUrlPorId(String id) {
        ObjectId objectId = new ObjectId(id);
        Document doc = collection.find(Filters.eq("_id", objectId)).first();
        if (doc != null) {
            UrlAcortada url = new UrlAcortada(
                    doc.getString("urlOriginal"),
                    doc.getString("urlCorta"),
                    doc.getString("usuarioId"),
                    doc.getDate("fechaCreacion")
            );
            url.setId(doc.getObjectId("_id"));
            url.setAccesos(doc.getInteger("accesos", 0));
            List<Document> historialDocs = doc.getList("historialAccesos", Document.class);
            if (historialDocs != null) {
                List<Acceso> historial = new ArrayList<>();
                for (Document accesoDoc : historialDocs) {
                    Acceso acceso = new Acceso();
                    acceso.setFecha(accesoDoc.getDate("fecha"));
                    acceso.setIp(accesoDoc.getString("ip"));
                    acceso.setNavegador(accesoDoc.getString("navegador"));
                    acceso.setSistemaOperativo(accesoDoc.getString("sistemaOperativo"));
                    acceso.setDominio(accesoDoc.getString("dominio"));
                    historial.add(acceso);
                }
                url.setHistorialAccesos(historial);
            }
            return url;
        }
        return null;
    }

}
