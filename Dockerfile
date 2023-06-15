FROM --platform=linux/arm32/v7 openjdk:17-oracle
VOLUME /tmp
COPY target/softue-0.0.1-SNAPSHOT.jar /app/demo.jar
ENTRYPOINT ["java", "-jar", "/app/demo.jar"]
