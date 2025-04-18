# Use a base image with Java
FROM openjdk:21
WORKDIR /app
COPY target/discovery-server-*.jar discovery-server.jar
EXPOSE 8761
ENTRYPOINT ["java", "-jar", "discovery-server.jar"]
