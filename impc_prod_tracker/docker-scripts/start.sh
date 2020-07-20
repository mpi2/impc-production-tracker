#!/bin/bash
set -e
[[ -z "${TRACKER_PORT}" ]] && port=8080 || port="${TRACKER_PORT}"

if [ -z "${DOCKER_PRODUCTION}" ]; then

	java -Djava.security.egd=file:/dev/./urandom -jar app.jar --server.port="${port}" --spring.profiles.active=docker

else

	if [ -e /etc/dbserver/db_config ]; then
	  export $(cat /etc/dbserver/db_config | awk -F ": " '{print $1"="$2 }')
	fi
	if [ -e /etc/aap/domain ]; then
	  export AAP_DOMAIN=$(cat /etc/aap/domain)
	fi
	if [ -e /etc/dbaccess/db_password ]; then
	  export TRACKER_POSTGRESQL_USER_PASSWORD=$(cat /etc/dbaccess/db_password)
	fi

	java -Djava.security.egd=file:/dev/./urandom -jar app.jar --server.port="${port}" --spring.profiles.active=dockerproduction

fi