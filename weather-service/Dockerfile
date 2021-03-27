FROM openjdk:8
COPY target/weather-service-0.0.1-SNAPSHOT.jar /weather-service.jar
EXPOSE 8080/tcp
ENTRYPOINT ["java", "-jar", "/weather-service.jar"]
