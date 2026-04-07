FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /app

COPY pom.xml .

RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw dependency:go-offline -B || true


COPY .mvn/ .mvn/
COPY mvnw .

RUN chmod +x mvnw


COPY src/ src/


RUN --mount=type=cache,target=/root/.m2 \
    ./mvnw package -DskipTests -B


FROM eclipse-temurin:21-jre-alpine AS runtime

WORKDIR /app


COPY --from=builder /app/target/SFC_TechUp_Futbol-0.0.1-SNAPSHOT.jar app.jar


EXPOSE 8080
EXPOSE 8443



ENTRYPOINT ["java", "-jar", "app.jar"]