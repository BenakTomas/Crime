baseDir="$1/sparql"
isql 1111 dba dba "${baseDir}/updateVersions_TrestneCiny.sparql" "${baseDir}/linkCrimesAndDepartments.sparql" "${baseDir}/updateObjasnenost.sparql"