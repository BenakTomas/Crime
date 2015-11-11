
baseDir="${CRIME_HOME}"
sparqlDir="${baseDir}/sparql"

isql 1111 dba dba "${sparqlDir}/updateVersions_Utvary.sparql" "${sparqlDir}/linkCrimesAndDepartments.sparql"