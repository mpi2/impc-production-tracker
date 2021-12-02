FROM maven:3-amazoncorretto-11 as build

COPY pom.xml /usr/src/gentar/
COPY dto/pom.xml /usr/src/gentar/dto/
COPY core/pom.xml /usr/src/gentar/core/
COPY rest-api/pom.xml /usr/src/gentar/rest-api/

COPY dto/src /usr/src/gentar/dto/src
COPY core/src /usr/src/gentar/core/src
COPY rest-api/src /usr/src/gentar/rest-api/src

RUN mvn -f /usr/src/gentar/pom.xml clean package -P test


FROM amazoncorretto:11-alpine as jre-build

# Create a custom Java runtime
RUN $JAVA_HOME/bin/jlink \
         --add-modules ALL-MODULE-PATH \
         --strip-debug \
         --no-man-pages \
         --no-header-files \
         --compress=2 \
         --output /tmp/java-jre


# The final image should have a minimal number of layers
FROM amazoncorretto:11-alpine

VOLUME /tmp

RUN find /usr/lib/jvm/default-jvm -delete
COPY --from=jre-build /tmp/java-jre $JAVA_HOME

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
