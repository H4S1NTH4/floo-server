# Use official OpenJDK base image


# Set working directory


# Copy the built JAR file (you'll need to build first)


# Expose the port your app runs on


# Command to run the application


FROM openjdk:21
WORKDIR /app
COPY target/restaurant-service-0.0.1-SNAPSHOT.jar restaurant-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "restaurant-service.jar"]
