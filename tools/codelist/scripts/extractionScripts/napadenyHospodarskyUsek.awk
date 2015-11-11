#!/bin/awk -f
BEGIN{
	FS="#"
}
{
	if(NR > 1)
	{
		sub(/^[[:space:]]*/, "", $1)
		sub(/[[:space:]]*$/, "", $1)
		sub(/^[[:space:]]*/, "", $2)
		sub(/[[:space:]]*$/, "", $2)
		printf "crimestatistics-code-codelist-hash:%s a skos:Concept, crimestatistics-code-codelist-slash:conceptClass ;\n", $1
		printf "\tskos:prefLabel \"%s\" ;\n", $2
		printf "\tskos:inScheme crimestatistics-code-codelist-slash:%s ;\n", codeListName
		printf "\tskos:notation \"%s\" ;\n", $1
		printf "\n"
		printf "\trdfs:label \"%s\" ;\n", $2
		printf "\t.\n"
	}
}