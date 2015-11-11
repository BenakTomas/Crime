 #!/bin/sh
baseDir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )";
fromDir="$1";

if [ $# -eq 2 ]
then
	year=$2;
	ontologyDir="${baseDir}/${year}/ontology";
else
	ontologyDir="${baseDir}/ontology";
fi

rm "${ontologyDir}/"*.ttl 2> /dev/null;

find "$fromDir/ontology" -name '*.ttl' -print0 | xargs -0 -I file cp file "${ontologyDir}";
find "$fromDir/resource/dataset/definitions" -name '*.ttl' -print0 | xargs -0 -I file cp file "${ontologyDir}";