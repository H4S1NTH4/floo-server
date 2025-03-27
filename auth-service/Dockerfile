# Use a base image with Java
FROM openjdk:21
WORKDIR /app
COPY target/auth-service-*.jar auth-service.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "auth-service.jar"]
