package edu.pucmm;

import java.io.Serializable;
import java.util.Date;

public class Acceso implements Serializable {
    private Date fecha;
    private String ip;
    private String navegador;
    private String sistemaOperativo;
    private String dominio; // Nuevo campo para el dominio del cliente

    public Acceso() {}

    public Acceso(Date fecha, String ip, String navegador, String sistemaOperativo, String dominio) {
        this.fecha = fecha;
        this.ip = ip;
        this.navegador = navegador;
        this.sistemaOperativo = sistemaOperativo;
        this.dominio = dominio;
    }

    // Constructor sin dominio (opcional)
    public Acceso(Date fecha, String ip, String navegador, String sistemaOperativo) {
        this(fecha, ip, navegador, sistemaOperativo, "Desconocido");
    }

    public Date getFecha() {
        return fecha;
    }
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getNavegador() {
        return navegador;
    }
    public void setNavegador(String navegador) {
        this.navegador = navegador;
    }
    public String getSistemaOperativo() {
        return sistemaOperativo;
    }
    public void setSistemaOperativo(String sistemaOperativo) {
        this.sistemaOperativo = sistemaOperativo;
    }
    public String getDominio() {
        return dominio;
    }
    public void setDominio(String dominio) {
        this.dominio = dominio;
    }

    @Override
    public String toString() {
        return "Acceso{" +
                "fecha=" + fecha +
                ", ip='" + ip + '\'' +
                ", navegador='" + navegador + '\'' +
                ", sistemaOperativo='" + sistemaOperativo + '\'' +
                ", dominio='" + dominio + '\'' +
                '}';
    }
}
