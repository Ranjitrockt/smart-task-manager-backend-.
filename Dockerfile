# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:17-jdk-focal

# Set the working directory in the container
WORKDIR /app

# Copy the Maven wrapper and the project object model file
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Download project dependencies
RUN ./mvnw dependency:go-offline

# Copy the project source
COPY src ./src

# Package the application
RUN ./mvnw package -DskipTests

# Expose the port the app runs on
EXPOSE 8081

# Command to run the application
CMD ["java", "-jar", "target/smart-task-manager-0.0.1-SNAPSHOT.jar"]