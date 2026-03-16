# Etapa 1: Construcción
FROM maven:3.9.9-eclipse-temurin-17-alpine AS build
WORKDIR /app
# Aquí cambiamos las rutas para que encuentre tus archivos
COPY mascotas/pom.xml .
COPY mascotas/src ./src
RUN mvn clean package -DskipTests

# Etapa 2: Imagen final
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Puerto que usa Spring Boot
EXPOSE 8080

# Comando para ejecutar activando el perfil prod
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod", "--server.port=8080"]
