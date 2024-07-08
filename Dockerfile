# Stage 1: Build stage
FROM gradle:7.6-jdk8 AS build

# Set the working directory
WORKDIR /app

# Copy the project files to the container
COPY . .

# Build the application (adjust for your specific build tool, here using Gradle)
RUN gradle build --no-daemon

# Stage 2: Runtime stage
FROM openjdk:22-jdk-slim

# Create a non-root user to run the application
RUN useradd -m -s /bin/bash appuser

# Set the working directory
WORKDIR /app

# Copy the built jar file
