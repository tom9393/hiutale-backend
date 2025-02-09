FROM eclipse-temurin:21.0.1_12-jdk-alpine AS build
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
CMD ["java", "-jar", "app.jar"]