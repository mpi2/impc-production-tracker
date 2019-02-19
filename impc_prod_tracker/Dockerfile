FROM maven:3.5-jdk-11-slim as build

# Resolve all the dependencies and cache them to save a LOT of time
COPY pom.xml /usr/src/myapp/
RUN mvn -f /usr/src/myapp/pom.xml dependency:resolve dependency:resolve-plugins

# Build the application - usually this is the only part that gets rebuilt locally - use offline mode and skip tests
COPY src /usr/src/myapp/src
RUN mvn -f /usr/src/myapp/pom.xml clean package -DskipTests -o


# The final image should have a minimal number of layers
FROM openjdk:11.0.2-jre-slim-stretch

VOLUME /tmp

COPY  --from=build /usr/src/myapp/target/*.jar app.jar
COPY docker-scripts/start.sh /

RUN ["chmod", "+x", "/start.sh"]

ENTRYPOINT ["/start.sh"]