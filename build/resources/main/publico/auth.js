function verificarAutenticacion() {
    const token = localStorage.getItem('token');  // Busca el token en el localStorage
    if (!token) {
        alert("No has iniciado sesión.");
        window.location.href = "/login.html";  // Si no hay token, redirige al login
    }
}
