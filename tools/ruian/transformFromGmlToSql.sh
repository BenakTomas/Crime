#! /bin/sh

baseDir="$1";
inputType="$2";
versionId="$3";
validFrom="$4"

sourceSubDir="source/vfr/extracted";

"${toolsDir}"/transformFromXmlToSql.sh "${baseDir}" "${sourceSubDir}" "${inputType}" "${versionId}" "${validFrom}";