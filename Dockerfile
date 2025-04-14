# Usa una imagen de Java 21 como base
FROM eclipse-temurin:21

# Crea una carpeta en el contenedor llamada /app
WORKDIR /app

# Copia el archivo .jar compilado de tu app al contenedor
COPY build/libs/*.jar app.jar

# Expone el puerto (ajústalo si tu app usa otro)
EXPOSE 7000

# Comando para ejecutar tu app
ENTRYPOINT ["java", "-jar", "app.jar"]
