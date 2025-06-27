# Stage 1: build with Maven CLI
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy only POM to leverage layer cache for dependencies
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code and run the build
COPY src ./src
RUN mvn package -DskipTests

# Stage 2: runtime image
FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser
WORKDIR /app

# Pull the fat JAR from the builder
COPY --from=builder /app/target/product-catalog-manager-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
