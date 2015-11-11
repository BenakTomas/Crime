#! /bin/sh

scriptPath="$1";
logFilePath="$2";
errorFilePath="$3";

psql -h localhost -p 5432 -U postgres -d gisdb -f "${scriptPath}" > "${logFilePath}" 2> "${errorFilePath}"; 