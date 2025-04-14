package edu.pucmm;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

/**
 * Repositorio para obtener estadísticas globales de todas las URLs.
 */
public class StatsRepository {

    private static final MongoCollection<Document> collection =
            MongoDBConnection.getDatabase().getCollection("urls");

    /**
     * Retorna un objeto DashboardStats con información agregada de todas las URLs.
     */
    public static DashboardStats obtenerEstadisticasGlobales() {
        DashboardStats stats = new DashboardStats();

        // 1) Total de URLs (documentos en la colección "urls")
        long totalUrls = collection.countDocuments();
        stats.setTotalUrls(totalUrls);

        // 2) Total de hits (sumatoria del campo "accesos")
        long totalHits = 0;

        // Para agrupar accesos por día, navegador y sistema operativo
        Map<String, Integer> hitsByDay = new TreeMap<>();
        Map<String, Integer> hitsByBrowser = new HashMap<>();
        Map<String, Integer> hitsByOs = new HashMap<>();

        // Leemos todos los documentos de la colección
        FindIterable<Document> docs = collection.find();

        for (Document doc : docs) {
            // Accesos totales
            int accesos = doc.getInteger("accesos", 0);
            totalHits += accesos;

            // Historial de accesos (array de objetos)
            List<Document> historial = (List<Document>) doc.get("historialAccesos");
            if (historial != null) {
                for (Document h : historial) {
                    Date fecha = h.getDate("fecha");
                    // El campo "navegador" en la BD realmente guarda la cadena completa de user-agent
                    String fullUserAgent = h.getString("navegador");

                    // Parseamos la fecha para agrupar hits por día
                    if (fecha != null) {
                        LocalDate date = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        String dayStr = date.toString(); // "2025-04-05" por ejemplo
                        hitsByDay.put(dayStr, hitsByDay.getOrDefault(dayStr, 0) + 1);
                    }

                    // Obtenemos el nombre corto del navegador
                    String shortBrowser = parseBrowserName(fullUserAgent);
                    hitsByBrowser.put(shortBrowser, hitsByBrowser.getOrDefault(shortBrowser, 0) + 1);

                    // Obtenemos el nombre corto del sistema operativo
                    String shortOs = parseOsName(fullUserAgent);
                    hitsByOs.put(shortOs, hitsByOs.getOrDefault(shortOs, 0) + 1);
                }
            }
        }

        // Asignamos resultados a la instancia de DashboardStats
        stats.setTotalHits(totalHits);
        stats.setHitsByDay(hitsByDay);
        stats.setHitsByBrowser(hitsByBrowser);
        stats.setHitsByOs(hitsByOs);

        return stats;
    }

    public static DashboardStats obtenerEstadisticasPorUsuario(String usuarioId) {
        DashboardStats stats = new DashboardStats();

        // Filtrar documentos por el id de usuario (usar el campo "usuarioId")
        Document filtro = new Document("usuarioId", usuarioId);
        long totalUrls = collection.countDocuments(filtro);
        stats.setTotalUrls(totalUrls);

        long totalHits = 0;
        Map<String, Integer> hitsByDay = new TreeMap<>();
        Map<String, Integer> hitsByBrowser = new HashMap<>();
        Map<String, Integer> hitsByOs = new HashMap<>();

        FindIterable<Document> docs = collection.find(filtro);

        for (Document doc : docs) {
            int accesos = doc.getInteger("accesos", 0);
            totalHits += accesos;

            List<Document> historial = (List<Document>) doc.get("historialAccesos");
            if (historial != null) {
                for (Document h : historial) {
                    Date fecha = h.getDate("fecha");
                    String fullUserAgent = h.getString("navegador");

                    if (fecha != null) {
                        LocalDate date = fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                        String dayStr = date.toString();
                        hitsByDay.put(dayStr, hitsByDay.getOrDefault(dayStr, 0) + 1);
                    }

                    String shortBrowser = parseBrowserName(fullUserAgent);
                    hitsByBrowser.put(shortBrowser, hitsByBrowser.getOrDefault(shortBrowser, 0) + 1);

                    String shortOs = parseOsName(fullUserAgent);
                    hitsByOs.put(shortOs, hitsByOs.getOrDefault(shortOs, 0) + 1);
                }
            }
        }

        stats.setTotalHits(totalHits);
        stats.setHitsByDay(hitsByDay);
        stats.setHitsByBrowser(hitsByBrowser);
        stats.setHitsByOs(hitsByOs);

        return stats;
    }


    /**
     * Parsea la cadena user-agent y retorna un nombre de navegador más amigable.
     */
    private static String parseBrowserName(String userAgent) {
        if (userAgent == null) return "Desconocido";
        String ua = userAgent.toLowerCase();

        if (ua.contains("opr") || ua.contains("opera")) {
            return "Opera";
        } else if (ua.contains("edg")) {
            return "Edge";
        } else if (ua.contains("chrome")) {
            // Nota: "Chrome" aparece también en muchos user-agent que son "Edge",
            // por eso verificamos Edge primero
            return "Chrome";
        } else if (ua.contains("safari")) {
            return "Safari";
        } else if (ua.contains("firefox")) {
            return "Firefox";
        }
        return "Otros";
    }

    /**
     * Parsea la cadena user-agent y retorna un nombre de sistema operativo más amigable.
     */
    private static String parseOsName(String userAgent) {
        if (userAgent == null) return "Desconocido";
        String ua = userAgent.toLowerCase();

        if (ua.contains("windows")) return "Windows";
        if (ua.contains("mac os") || ua.contains("macintosh")) return "Mac OS";
        if (ua.contains("linux")) return "Linux";
        if (ua.contains("android")) return "Android";
        if (ua.contains("iphone") || ua.contains("ipad") || ua.contains("ios")) return "iOS";
        return "Otros";
    }
}
