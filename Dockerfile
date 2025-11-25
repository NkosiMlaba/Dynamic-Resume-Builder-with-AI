FROM openjdk:22-jdk-slim AS build

RUN apt-get update && apt-get install -y maven && apt-get clean && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn package -DskipTests

FROM openjdk:22-jdk-slim

WORKDIR /app

COPY --from=build /app/target/resume-builder-1.0-SNAPSHOT-webapi-jar-with-dependencies.jar ./resume-builder-webapi.jar
COPY --from=build /app/src/main/resources/static ./src/main/resources/static

EXPOSE 7000

ENTRYPOINT ["java", "-jar", "resume-builder-webapi.jar"]
