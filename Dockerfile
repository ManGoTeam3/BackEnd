# Base image
FROM openjdk:17-jdk

# Set working directory
WORKDIR /app

# Copy the JAR file from the build output directory to the container
COPY build/libs/*.jar Mango.jar
COPY src/main/resources/poetic-inkwell-401203-8aa220d61f5f.json poetic-inkwell-401203-8aa220d61f5f.json

# Expose port (if your Spring Boot application uses a different port, update it here)
EXPOSE 8080

# Command to run the Spring Boot application when the container starts
CMD ["java", "-jar", "Mango.jar"]
