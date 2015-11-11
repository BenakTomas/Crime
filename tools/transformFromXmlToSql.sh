#! /bin/sh

baseDir="${CRIME_HOME}"

inputType="$1";
tableName="$2"
joinColumn="$3"
inputFilePath="$4"
workingDir="$5"

toolsDir="${baseDir}/tools";

xsltDirPath="${baseDir}/xslt/${inputType}";
xsltTemplateFilePath="${baseDir}/xslt/${inputType}/template.xslt";
xsltFilePath="${workingDir}/${inputType}.xslt"

outFilePath="${workingDir}/${inputType}.sql";
logFilePath="${workingDir}/${inputType}.log";
errorFilePath="${workingDir}/${inputType}.error.log";

echo "${inputFilePath}"
"${toolsDir}/xslt.sh" "${baseDir}" "${inputFilePath}" "${xsltFilePath}" > "${outFilePath}";