# Stage 1: Build the application
FROM gradle:8-jdk17 AS build

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper and other required files
COPY gradle /app/gradle
COPY gradlew /app/
COPY build.gradle.kts /app/
COPY settings.gradle.kts /app/

# Download dependencies
RUN ./gradlew build -x test --parallel --no-daemon

# Copy the source code
COPY src /app/src

# Build the application
RUN ./gradlew clean bootJar -x test --no-daemon

# Stage 2: Create the runtime image
FROM eclipse-temurin:17-jdk-jammy

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file from the build stage
COPY --from=build /app/build/libs/*.jar app.jar

# Expose the port your app runs on
EXPOSE 8080

# Command to run the app
ENTRYPOINT ["java", "-jar", "/app/app.jar"]