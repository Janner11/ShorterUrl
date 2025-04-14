document.getElementById("login-form").addEventListener("submit", function(event) {
    event.preventDefault(); // Evita que la página se recargue

    const formData = new FormData(this);

    fetch('/login', {
        method: 'POST',
        body: new URLSearchParams(formData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.token) {
            localStorage.setItem('token', data.token); // Guardar el token
            alert("Login exitoso!");
            window.location.href = "/index.html"; // Redirigir a la página principal
        } else {
            alert("Usuario o contraseña incorrectos");
        }
    });
});
