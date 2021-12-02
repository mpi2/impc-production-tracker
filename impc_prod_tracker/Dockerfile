FROM maven:3-amazoncorretto-11 as build

COPY pom.xml /usr/src/gentar/
COPY dto/pom.xml /usr/src/gentar/dto/
COPY core/pom.xml /usr/src/gentar/core/
COPY rest-api/pom.xml /usr/src/gentar/rest-api/

COPY dto/src /usr/src/gentar/dto/src
COPY core/src /usr/src/gentar/core/src
COPY rest-api/src /usr/src/gentar/rest-api/src

RUN mvn -f /usr/src/gentar/pom.xml clean package -P test


# The final image should have a minimal number of layers
FROM amazoncorretto:11-alpine

RUN adduser \
    --disabled-password \
    --gecos "" \
    --no-create-home \
    --uid 10002 \
    containeruser \

VOLUME /tmp

COPY  --from=build /usr/src/gentar/rest-api/target/*.jar app.jar
RUN ["chown", "containeruser:containeruser", "app.jar"]

COPY docker-scripts/start.sh /
RUN ["chown", "containeruser:containeruser", "start.sh"]
RUN ["chmod", "+x", "/start.sh"]

ENTRYPOINT ["/start.sh"]
USER containeruser
