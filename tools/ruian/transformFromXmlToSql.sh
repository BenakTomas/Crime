#! /bin/sh

baseDir="$1";
sourceSubDir="$2";
inputType="$3";
versionId="$4";
validFrom="$5"
tableName="$6"
joinColumn="$7"

toolsDir="${baseDir}/tools";

validFrom_timestamp=$(date --date="${validFrom}" +"%s");

inputFilePath="${baseDir}/${sourceSubDir}/${versionId}/${inputType}.xml"

xsltDirPath="${baseDir}/xslt/${inputType}";
xsltTemplateFilePath="${xsltDirPath}/${inputType}_template.xslt";
xsltFilePath="${xsltDirPath}/${inputType}_${validFrom_timestamp}.xslt"

outFilePath="${baseDir}/sql/${inputType}/${inputType}_${validFrom_timestamp}.sql";
logFilePath="${baseDir}/sql/${inputType}/log/${inputType}_log_${validFrom_timestamp}.txt";
errorFilePath="${baseDir}/sql/${inputType}/log/${inputType}_error_${validFrom_timestamp}.txt";

sed -r 's|\$validFrom|'"${validFrom}"'|gI;' "${xsltTemplateFilePath}" > "${xsltFilePath}";

"${toolsDir}"/xslt.sh "${baseDir}" "${inputFilePath}" "${xsltFilePath}" > "${outFilePath}";

"${toolsDir}"/runSQLScript.sh "${outFilePath}" "${logFilePath}" "${errorFilePath}";

updateVersionsSqlScriptPath="${baseDir}/sql/scripts/updateVersions/updateVersions.sql";
updateVersionsSqlScriptPathForTable="${baseDir}/sql/scripts/updateVersions/updateVersions_${tableName}.sql";
updateVersionsSqlLogPath="${baseDir}/sql/scripts/updateVersions/log/updateVersions_${inputType}.log";
updateVersionsSqlErrorPath="${baseDir}/sql/scripts/updateVersions/log/updateVersions_${inputType}_error.log";

sed -r 's|\$table|'"${tableName}"'|gI; s|\$joinColumn|'"${joinColumn}"'|gI;' "${updateVersionsSqlScriptPath}" > "${updateVersionsSqlScriptPathForTable}";

"${toolsDir}"/runSQLScript.sh "${updateVersionsSqlScriptPathForTable}" "${updateVersionsSqlLogPath}" "${updateVersionsSqlErrorPath}";  