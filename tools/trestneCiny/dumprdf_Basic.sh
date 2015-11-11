#! /bin/sh

baseDir="$CRIME_HOME";
entityType="$1";
workingDir="$2"
d2rqHomePath="${D2RQ_HOME}";

dumpRDFScriptPath="${d2rqHomePath}/dump-rdf";
outputFilePath="${workingDir}/${entityType}.n3";
mappingFilePath="${baseDir}/d2rq/templates/${entityType}/mapping_template.ttl";

"${dumpRDFScriptPath}" --verbose -o "${outputFilePath}" "${mappingFilePath}";