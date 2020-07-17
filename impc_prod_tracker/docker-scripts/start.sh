#!/bin/bash
set -e
[[ -z "${TRACKER_PORT}" ]] && port=8080 || port="${TRACKER_PORT}"

[[ -z "${DOCKER_PRODUCTION}" ]] then
	java -Djava.security.egd=file:/dev/./urandom -jar app.jar --server.port="${port}" --spring.profiles.active=docker
else
	java -Djava.security.egd=file:/dev/./urandom -jar app.jar --server.port="${port}" --spring.profiles.active=dockerproduction
fi;