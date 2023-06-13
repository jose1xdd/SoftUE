FROM openjdk:17
VOLUME /tmp
EXPOSE 8080
ADD ./target/softue-0.0.1-SNAPSHOT.jar app.jar
COPY application.properties /app/application.properties
ENTRYPOINT ["java", "-jar", "app.jar"]
