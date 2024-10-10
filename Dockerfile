FROM openjdk:22-jdk-slim AS build

RUN apt-get update && apt-get install -y maven && apt-get clean && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY pom.xml ./
COPY src ./src

RUN mvn package -DskipTests -e -X

FROM openjdk:22-jdk-slim

WORKDIR /app

COPY --from=build /app/target/resume-builder-1.0-SNAPSHOT-webapi-jar-with-dependencies.jar ./resume-builder-webapi.jar

EXPOSE 5050

ENTRYPOINT ["java", "-jar", "resume-builder-webapi.jar"]
