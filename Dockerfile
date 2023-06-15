# Usa la imagen base de OpenJDK 17
FROM --platform=linux/arm64 openjdk:17-oracle

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Copia el archivo JAR del backend al contenedor
COPY target/softue-0.0.1-SNAPSHOT.jar app.jar

# Expone el puerto en el que se ejecuta la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación cuando el contenedor se inicie
CMD ["java", "-jar", "app.jar"]

