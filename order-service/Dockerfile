FROM openjdk:21
WORKDIR /app
COPY target/order-service-*.jar order-service.jar
EXPOSE 8082
ENTRYPOINT ["java", "-jar", "order-service.jar"]
