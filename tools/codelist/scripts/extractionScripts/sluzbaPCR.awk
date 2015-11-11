#!/bin/gawk -f
BEGIN{
	FS="#"
}
(/^[0-9]+/ || /^[[:alpha:]]/) && !/^[[:alpha:]][^[:space:]]+/{
	for(i=1; i <= NF; ++i)
	{
		sub(/^[[:space:]]*/, "", $i)
		sub(/[[:space:]]*$/, "", $i)
	}
	
	printf "crimestatistics-code-codelist-hash:%s a skos:Concept, crimestatistics-code-codelist-slash:conceptClass ;\n", $1
	printf "\tskos:prefLabel \"%s\"@cs ;\n", $2
	printf "\tskos:inScheme crimestatistics-code-codelist-slash:%s ;\n", codeListName
	printf "\tskos:notation \"%s\" ;\n", $1
	if($3 !~ /^[ 	]*$/)
	{
		printf "\tskos:scopeNote \"%s\" ;\n", $3
	}
	printf "\n"
	printf "\trdfs:label \"%s\"@cs ;\n", $2
	printf "\t.\n"
}