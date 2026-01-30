# Stage 1: Extraction stage
# Use an official JDK slim image for a smaller build footprint
FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR application
# Define the location of the compiled JAR file
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
# Extract the JAR layers using Spring Boot's layertools mode for optimized Docker caching
RUN java -Djarmode=layertools -jar application.jar extract

# Final stage
FROM eclipse-temurin:17-jre-jammy
WORKDIR application
# Copy extracted layers from the builder stage.
# Caching works best when dependencies (rarely changed) are copied before the application code.
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./
# Use JarLauncher to start the application from the extracted layers
ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
EXPOSE 8080