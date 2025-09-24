# --- Build Stage ---
FROM maven:3.9.11-eclipse-temurin-21 AS build
LABEL stage=builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src

RUN mvn clean package -DskipTests

# --- Run Stage ---
FROM eclipse-temurin:21-jre-noble

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 7777

ENTRYPOINT ["java", "-jar", "app.jar"]
