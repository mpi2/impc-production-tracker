FROM maven:3-amazoncorretto-25 as build

COPY pom.xml /usr/src/gentar/
COPY dto/pom.xml /usr/src/gentar/dto/
COPY core/pom.xml /usr/src/gentar/core/
COPY rest-api/pom.xml /usr/src/gentar/rest-api/

COPY dto/src /usr/src/gentar/dto/src
COPY core/src /usr/src/gentar/core/src
COPY rest-api/src /usr/src/gentar/rest-api/src

RUN  MAVEN_OPTS=" --add-opens java.base/sun.nio.ch=ALL-UNNAMED --add-opens java.base/java.io=ALL-UNNAMED " mvn -f /usr/src/gentar/pom.xml clean package -P test


# The final image should have a minimal number of layers
FROM amazoncorretto:25-alpine

VOLUME /tmp

COPY  --from=build /usr/src/gentar/rest-api/target/*.jar app.jar
COPY docker-scripts/start.sh /

RUN adduser \
    --disabled-password \
    --gecos "" \
    --no-create-home \
    --uid 20256 \
    containeruser && \
    chown containeruser:containeruser app.jar && \
    chown containeruser:containeruser start.sh && \
    chmod +x /start.sh

ENTRYPOINT ["/start.sh"]
USER containeruser
