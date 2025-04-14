package edu.pucmm;

import org.bson.types.ObjectId;
import java.io.Serializable;

public class Usuario implements Serializable {

    private ObjectId id;
    private String username;
    private String nombre;
    private String password;
    private Boolean admin;
    private Boolean activo;

    public Usuario() {}

    // 🔹 Constructor con parámetros
    public Usuario(String username, String nombre, String password, Boolean admin, Boolean activo) {
        this.id = new ObjectId();
        this.username = username;
        this.nombre = nombre;
        this.password = password;
        this.admin = admin;
        this.activo = activo;
    }

    // 🔹 Getters y Setters
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getAdmin() {
        return admin;
    }

    public void setAdmin(Boolean admin) {
        this.admin = admin;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", nombre='" + nombre + '\'' +
                ", admin=" + admin +
                ", activo=" + activo +
                '}';
    }
}
