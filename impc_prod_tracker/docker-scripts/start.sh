#!/bin/bash
set -e
[[ -z "${TRACKER_PORT}" ]] && port=8080 || port="${TRACKER_PORT}"

java -Djava.security.egd=file:/dev/./urandom -jar app.jar -Dspring.profiles.active=docker --server.port="${port}"
