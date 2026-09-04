# ---------- ETAPA 1: build ----------
FROM gradle:8.10-jdk17 AS build
WORKDIR /app

COPY build.gradle settings.gradle* ./
COPY src ./src

RUN gradle clean bootJar -x test --no-daemon
RUN cp build/libs/*-SNAPSHOT.jar /app/app.jar 2>/dev/null || cp build/libs/*-1.0.0.jar /app/app.jar

# ---------- ETAPA 2: run ----------
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/app.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
