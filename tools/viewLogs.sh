#! /bin/sh

find "${CATALINA_HOME}/logs" -name '*'`date +%Y-%m-%d`'*' -print0 | xargs -0 -I file pspad file