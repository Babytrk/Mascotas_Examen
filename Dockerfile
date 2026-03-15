# Paso 1: Usar Maven para compilar el código
FROM maven:3.8.5-openjdk-17 AS build
COPY mascotas/ .
RUN mvn clean package -DskipTests

# Paso 2: Usar Java para correr el archivo .jar resultante
FROM openjdk:17.0.1-jdk-slim
COPY --from=build /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
