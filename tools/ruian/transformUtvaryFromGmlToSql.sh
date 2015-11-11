#! /bin/sh

baseDir="$1";
versionId="$2";
validFrom="$3"

inputType="utvary";
sourceSubDir="source/utvary";
toolsDir="${baseDir}/tools";
utvarySqlScriptPath="${baseDir}/sql/utvary/updateVersions.sql";
utvarySqlLogPath="${baseDir}/sql/utvary/log/updateVersions.log";
utvarySqlErrorPath="${baseDir}/sql/utvary/log/updateVersions_error.log";

"${toolsDir}"/transformFromXmlToSql.sh "${baseDir}" "${sourceSubDir}" "${inputType}" "${versionId}" "${validFrom}" "utvar_geom" "ut"; 