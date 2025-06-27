# Stage 1: build with Maven
FROM maven:3.9.4-eclipse-temurin-17 AS builder
WORKDIR /app

# Copy only Maven config first for better cache
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy source and build
COPY src ./src
RUN ./mvnw package -DskipTests

# Stage 2: runtime image
FROM eclipse-temurin:17-jre-alpine
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser
WORKDIR /app

# Copy the fat JAR from the builder stage
COPY --from=builder /app/target/product-catalog-manager-*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]
