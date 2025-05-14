# Use a base image with Java
FROM openjdk:21
WORKDIR /app
COPY target/payment-service-*.jar payment-service.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "payment-service.jar"]
