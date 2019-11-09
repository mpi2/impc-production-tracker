#!/bin/bash
set -e
[[ -z "${TRACKER_PORT}" ]] && port=8080 || port="${TRACKER_PORT}"

if [ -z ${REST_BASE_PATH+x} ]; then
  # REST_BASE_PATH is unset
  # see: https://stackoverflow.com/questions/3601515/how-to-check-if-a-variable-is-set-in-bash
  # https://pubs.opengroup.org/onlinepubs/9699919799/utilities/V3_chap02.html#tag_18_06_02
  REST_BASE_PATH_CONF="";
else
  REST_BASE_PATH_CONF=" --spring.data.rest.base-path=${REST_BASE_PATH} ";
fi

java -Djava.security.egd=file:/dev/./urandom -jar app.jar --server.port="${port}" "${REST_BASE_PATH_CONF}" --spring.profiles.active=docker
