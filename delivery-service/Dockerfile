FROM openjdk:21
WORKDIR /app
COPY target/delivery-service-*.jar delivery-service.jar
EXPOSE 8085
ENTRYPOINT ["java", "-jar", "delivery-service.jar"]
