# Stage 1: Build the application using a Maven base image
FROM maven:3.9.6-eclipse-temurin-21-jammy AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src
COPY swagger.yaml .

# Package the application (skip tests for faster build during deploy)
RUN mvn clean package -DskipTests

# Stage 2: Create the minimal runtime image
FROM eclipse-temurin:21-jre-jammy

# Set the working directory
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/loanstreet-backend-0.0.1-SNAPSHOT.jar app.jar

# Expose the application port
EXPOSE 8080

# Run the jar file
ENTRYPOINT ["java", "-jar", "app.jar"]