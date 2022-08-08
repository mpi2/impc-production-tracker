#!/bin/ash
set -e
[[ -z "${TRACKER_PORT}" ]] && port=8080 || port="${TRACKER_PORT}"

if [ -z "${DOCKER_PRODUCTION}" ]; then

  if [ -z "${GENTAR_SCHEMA}" ]; then

    if [ -z "${EBI_PROXY}" ]; then

      java -Djava.security.egd=file:/dev/./urandom -jar app.jar \
        --server.port="${port}" --spring.profiles.active=docker
        --add-opens java.base/java.time=ALL-UNNAMED

    else

      java -Djava.security.egd=file:/dev/./urandom \
        -Dhttps.proxyHost=hx-wwwcache.ebi.ac.uk \
        -Dhttps.proxyPort=3128 \
        -Dhttp.proxyHost=hx-wwwcache.ebi.ac.uk \
        -Dhttp.proxyPort=3128 \
        -Dhttp.nonProxyHosts=*.ebi.ac.uk\|localhost\|127.0.0.1 \
        -Dftp.proxyHost=hx-wwwcache.ebi.ac.uk \
        -Dftp.proxyPort=3128 \
        -Dftp.nonProxyHosts=*.ebi.ac.uk\|localhost\|127.0.0.1 \
        -jar app.jar \
        --server.port="${port}" --spring.profiles.active=docker
        --add-opens java.base/java.time=ALL-UNNAMED

    fi

  else

    if [ -z "${EBI_PROXY}" ]; then

      java -Djava.security.egd=file:/dev/./urandom -jar app.jar \
        --server.port="${port}" --spring.profiles.active=dockergentarschema
        --add-opens java.base/java.time=ALL-UNNAMED

    else

      java -Djava.security.egd=file:/dev/./urandom \
        -Dhttps.proxyHost=hx-wwwcache.ebi.ac.uk \
        -Dhttps.proxyPort=3128 \
        -Dhttp.proxyHost=hx-wwwcache.ebi.ac.uk \
        -Dhttp.proxyPort=3128 \
        -Dhttp.nonProxyHosts=*.ebi.ac.uk\|localhost\|127.0.0.1 \
        -Dftp.proxyHost=hx-wwwcache.ebi.ac.uk \
        -Dftp.proxyPort=3128 \
        -Dftp.nonProxyHosts=*.ebi.ac.uk\|localhost\|127.0.0.1 \
        -jar app.jar \
        --server.port="${port}" --spring.profiles.active=dockergentarschema
        --add-opens java.base/java.time=ALL-UNNAMED

    fi

  fi

else

  if [ -z "${EBI_PROXY}" ]; then

    java -Djava.security.egd=file:/dev/./urandom -jar app.jar \
      --server.port="${port}" --spring.profiles.active=dockerproduction
      --add-opens java.base/java.time=ALL-UNNAMED

  else

    java -Djava.security.egd=file:/dev/./urandom \
      -Dhttps.proxyHost=hx-wwwcache.ebi.ac.uk \
      -Dhttps.proxyPort=3128 \
      -Dhttp.proxyHost=hx-wwwcache.ebi.ac.uk \
      -Dhttp.proxyPort=3128 \
      -Dhttp.nonProxyHosts=*.ebi.ac.uk\|localhost\|127.0.0.1 \
      -Dftp.proxyHost=hx-wwwcache.ebi.ac.uk \
      -Dftp.proxyPort=3128 \
      -Dftp.nonProxyHosts=*.ebi.ac.uk\|localhost\|127.0.0.1 \
      -jar app.jar \
      --server.port="${port}" --spring.profiles.active=dockerproduction
      --add-opens java.base/java.time=ALL-UNNAMED

  fi

fi
