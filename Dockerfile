# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:22-jdk

# Set the working directory in the container
WORKDIR /app

# Copy the build files from the host machine to the container
COPY build/libs/olympics-0.0.1-SNAPSHOT.jar olympics.jar

# Expose the port the application will run on
EXPOSE 8080

# Define the command to run the application
CMD ["java", "-jar", "olympics.jar"]
