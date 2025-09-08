FROM maven:3.9.4-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /app
COPY --from=build target/*.jar app.jar
#ENV MAVEN_OPTS="-Xmx1024m -XX:MaxPermSize=256m"
ENTRYPOINT ["java", "-jar", "app.jar"]