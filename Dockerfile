FROM arm32v7/eclipse-temurin:17-jdk
VOLUME /tmp
COPY target/softue-0.0.1-SNAPSHOT.jar /app/demo.jar
ENTRYPOINT ["java", "-jar", "/app/demo.jar"]
