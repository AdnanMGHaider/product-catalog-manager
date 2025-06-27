# ---------- Stage 1: build ----------
FROM maven:3.9.4-eclipse-temurin-21 AS builder
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package -DskipTests          # produces 21-bytecode

# ---------- Stage 2: runtime ----------
FROM eclipse-temurin:21-jre-alpine   # ← 21 instead of 17
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser
WORKDIR /app

COPY --from=builder /app/target/product-catalog-manager-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
