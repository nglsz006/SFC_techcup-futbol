# ================================
# Stage 1: Build
# ================================
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

# Copiamos el pom y descargamos dependencias primero (cache layer)
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw dependency:go-offline -B || true

# Copiamos el wrapper de Maven
COPY .mvn/ .mvn/
COPY mvnw .

RUN chmod +x mvnw

# Copiamos el código fuente
COPY src/ src/

# Compilamos saltando tests (los tests corren en CI, no en la imagen)
RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw package -DskipTests -B

# ================================
# Stage 2: Runtime
# ================================
FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app

# Copiamos solo el JAR generado
COPY --from=builder /app/target/SFC_TechUp_Futbol-0.0.1-SNAPSHOT.jar app.jar

# Puerto que expone la app
EXPOSE 8080

# Ejecutamos con profile prod activo
ENTRYPOINT ["java", "-Dspring.profiles.active=prod", "-jar", "app.jar"]