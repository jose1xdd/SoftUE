# Utiliza una imagen base de Java 17
FROM openjdk:17-jdk-alpine

# Establece el directorio de trabajo
WORKDIR /app

# Copia los archivos necesarios al contenedor
COPY target/softue-0.0.1-SNAPSHOT.jar /app/demo.jar

# Exponer el puerto 8080
EXPOSE 8080

# Comando para iniciar la aplicación
CMD ["java", "-jar", "demo.jar"]
