# Stage 1: Build the app using Maven
FROM maven:latest AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Stage 2: Run the application
FROM eclipse-temurin:21.0.1_12-jdk-alpine
WORKDIR /app
COPY --from=build /app/target/hiutaleapp.jar hiutaleapp.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "hiutaleapp.jar"]
