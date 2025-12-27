


# Use an official Eclipse Temurin runtime as a parent image
FROM eclipse-temurin:17-jdk-focal

# Set the working directory in the container
WORKDIR /app

# Copy the project files from the sub-directory
COPY Smart_Task_Maneger/.mvn/ .mvn
COPY Smart_Task_Maneger/mvnw .
COPY Smart_Task_Maneger/pom.xml .
COPY Smart_Task_Maneger/src ./src

# Download project dependencies
RUN ./mvnw dependency:go-offline

# Package the application
RUN ./mvnw package -DskipTests

# Expose the port the app runs on
EXPOSE 8081

# Command to run the application
CMD ["java", "-jar", "target/smart-task-manager-0.0.1-SNAPSHOT.jar"]