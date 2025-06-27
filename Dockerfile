# Dockerfile

# 1. Use a lightweight Java 17 runtime
FROM eclipse-temurin:17-jre-alpine

# 2. Create a non-root user for security
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# 3. Set working directory
WORKDIR /app

# 4. Copy the built JAR into the image
#    - Assumes your Maven artifactId is "product-catalog-manager"
#    - This wildcard picks up the versioned JAR (e.g. target/product-catalog-manager-0.0.1-SNAPSHOT.jar)
ARG JAR_FILE=target/product-catalog-manager-*.jar
COPY ${JAR_FILE} app.jar

# 5. Expose the port your app listens on
EXPOSE 8080

# 6. Run the JAR
ENTRYPOINT ["java", "-jar", "app.jar"]
