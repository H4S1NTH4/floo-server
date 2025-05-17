# Use a base image with Java
FROM openjdk:21
WORKDIR /app
COPY target/email-service-*.jar email-service.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "email-service.jar"]
