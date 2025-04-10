# Use a base image with Java
FROM openjdk:21
WORKDIR /app
COPY target/api-gateway-*.jar api-gateway.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "api-gateway.jar"]
