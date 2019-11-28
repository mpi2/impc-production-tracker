FROM maven:3.5-jdk-11-slim as build

COPY pom.xml /usr/src/gentar/
COPY dto/pom.xml /usr/src/gentar/dto/
COPY core/pom.xml /usr/src/gentar/core/
COPY rest-api/pom.xml /usr/src/gentar/rest-api/

COPY dto/src /usr/src/gentar/dto/src
COPY core/src /usr/src/gentar/core/src
COPY rest-api/src /usr/src/gentar/rest-api/src

RUN mvn -f /usr/src/gentar/pom.xml clean package


# The final image should have a minimal number of layers
FROM openjdk:11.0.2-jre-slim-stretch

VOLUME /tmp

COPY  --from=build /usr/src/gentar/target/*.jar app.jar
COPY docker-scripts/start.sh /

RUN ["chmod", "+x", "/start.sh"]

ENTRYPOINT ["/start.sh"]