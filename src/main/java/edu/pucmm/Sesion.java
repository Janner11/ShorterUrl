package edu.pucmm;

public class Sesion {
    // Atributo para almacenar el token JWT
    private String token;
    // Usuario asociado a la sesión
    private Usuario usuario;

    // Constructor vacío (necesario para frameworks de serialización/deserialización)
    public Sesion() {}

    // Constructor con parámetros para crear una sesión de forma directa
    public Sesion(String token, Usuario usuario) {
        this.token = token;
        this.usuario = usuario;
    }

    // Getters y Setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Sesion{" +
                "token='" + token + '\'' +
                ", usuario=" + usuario +
                '}';
    }
}
