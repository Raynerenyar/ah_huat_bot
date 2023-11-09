FROM maven:3.8.3-openjdk-17 AS build

WORKDIR /app

## FIRST STAGE ##
COPY src /app/src
COPY pom.xml /app

# RUN --mount=type=secret,id=_env,dst=/etc/secrets/.env cat /etc/secrets/.env
RUN mvn -f /app/pom.xml clean package -Dmaven.test.skip


## SECOND STAGE docker pull eclipse-temurin:17 ##
FROM eclipse-temurin:17

COPY --from=build /app/target/toto-0.0.1-SNAPSHOT.jar /app/target/toto-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/target/toto-0.0.1-SNAPSHOT.jar"]