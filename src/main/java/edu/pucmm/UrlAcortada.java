package edu.pucmm;

import org.bson.types.ObjectId;
import java.util.Date;
import java.util.List;

public class UrlAcortada {
    private ObjectId id;
    private String urlOriginal;
    private String urlCorta;
    private String usuarioId;
    private int accesos;
    private List<Acceso> historialAccesos;
    private Date fechaCreacion;  // Agregado el campo fechaCreacion
    private String imagenPreviewBase64; //

    // Constructor
    public UrlAcortada() {}

    // Constructor con parámetros
    public UrlAcortada(String urlOriginal, String urlCorta, String usuarioId, Date fechaCreacion) {
        this.urlOriginal = urlOriginal;
        this.urlCorta = urlCorta;
        this.usuarioId = usuarioId;
        this.fechaCreacion = fechaCreacion;
        this.accesos = 0;
        this.historialAccesos = null;

    }

    // Getters y Setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUrlOriginal() {
        return urlOriginal;
    }

    public void setUrlOriginal(String urlOriginal) {
        this.urlOriginal = urlOriginal;
    }

    public String getUrlCorta() {
        return urlCorta;
    }

    public void setUrlCorta(String urlCorta) {
        this.urlCorta = urlCorta;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public int getAccesos() {
        return accesos;
    }

    public void setAccesos(int accesos) {
        this.accesos = accesos;
    }

    public List<Acceso> getHistorialAccesos() {
        return historialAccesos;
    }

    public void setHistorialAccesos(List<Acceso> historialAccesos) {
        this.historialAccesos = historialAccesos;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;  // Asegúrate de tener este setter
    }
    public String getImagenPreviewBase64() {
        return imagenPreviewBase64;
    }

    public void setImagenPreviewBase64(String imagenPreviewBase64) {
        this.imagenPreviewBase64 = imagenPreviewBase64;
    }

    @Override
    public String toString() {
        return "UrlAcortada{" +
                "id=" + id +
                ", urlOriginal='" + urlOriginal + '\'' +
                ", urlCorta='" + urlCorta + '\'' +
                ", usuarioId='" + usuarioId + '\'' +
                ", accesos=" + accesos +
                ", fechaCreacion=" + fechaCreacion +
                '}';
    }
}
