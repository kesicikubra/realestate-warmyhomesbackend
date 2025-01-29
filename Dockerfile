
# Build aşaması
FROM maven:3.8.1-openjdk-17 AS build
RUN mkdir -p workspace
WORKDIR /workspace
COPY pom.xml /workspace/
COPY src /workspace/src
RUN mvn clean package -Dmaven.test.skip

# Run aşaması
FROM amazoncorretto:17
VOLUME /app
COPY --from=build /workspace/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app.jar"]
