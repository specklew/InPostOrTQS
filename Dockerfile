# Use the official OpenJDK 17 Alpine base image
FROM openjdk:17-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the Spring Boot executable JAR file into the container
COPY target/inpostor.jar .

# Expose the port on which your Spring Boot application listens
EXPOSE 8080

# Define the command to run your application
CMD ["java", "-jar", "inpostor.jar"]
