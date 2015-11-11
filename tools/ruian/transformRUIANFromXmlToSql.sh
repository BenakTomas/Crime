#! /bin/sh

baseDir="$1";
inputType="$2";
versionId="$3";
validFrom="$4"
tableName="$5"
joinColumn="$6"


sourceSubDir="source/vfr/extracted";
toolsDir="${baseDir}/tools";

"${toolsDir}"/transformFromXmlToSql.sh "${baseDir}" "${sourceSubDir}" "${inputType}" "${versionId}" "${validFrom}" "${tableName}" "${joinColumn}";