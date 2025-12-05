FROM eclipse-temurin:17 AS build

WORKDIR /workspace

COPY mvnw pom.xml ./
COPY .mvn .mvn
COPY src src

RUN chmod +x mvnw && ./mvnw -DskipTests clean package -Pprod -DskipTests

FROM eclipse-temurin:17-jre-alpine

# Set the working directory in the container
WORKDIR /app

# Copy JAR from build stage
COPY --from=build /workspace/target/*.jar app.jar

# Create non-root user for security (hardening)
RUN addgroup -S spring && adduser -S spring -G spring

# Change ownership of application files
RUN chown -R spring:spring /app

# Switch to non-root user
USER spring

# Expose the port that your application will run on
EXPOSE 8080

# Specify the command to run on container start
ENTRYPOINT ["java","-jar","app.jar"]
