FROM eclipse-temurin:17 AS build

WORKDIR /workspace

COPY mvnw pom.xml ./
COPY .mvn .mvn
COPY src src

RUN chmod +x mvnw && ./mvnw -DskipTests clean package -Pprod -DskipTests

FROM eclipse-temurin:17-jre

# Set the working directory in the container
WORKDIR /devops-atv04

ARG JAR_FILE=target/*.jar
COPY --from=build /workspace/target/*.jar /devops-atv04/app.jar

# Expose the port that your application will run on
EXPOSE 8080

# Specify the command to run on container start
ENTRYPOINT ["java","-jar","/devops-atv04/app.jar"]
