#! /bin/sh

entityType="$1";
workingDir="$2"

outputFilePath="${workingDir}/${entityType}_versioned.n3";
mappingFilePath="${CRIME_HOME}/d2rq/templates/${entityType}/mapping_template_versioned.ttl";

"${D2RQ_HOME}/dump-rdf" --verbose -o "${outputFilePath}" "${mappingFilePath}";