#!/bin/gawk -f
BEGIN{
	FS="#"
}
(/^[0-9]+#/ || /^[a-zA-Z]#/){
	for(i=1; i <= NF; ++i)
	{
		sub(/^[[:space:]]*/, "", $i)
		sub(/[[:space:]]*$/, "", $i)
	}
	
	printf "crimestatistics-code-codelist-hash:%s a skos:Concept, crimestatistics-code-codelist-slash:conceptClass ;\n", $1
	printf "\tskos:prefLabel \"%s\"@cs ;\n", $2
	printf "\tskos:inScheme crimestatistics-code-slash:%s ;\n", codeListName
	printf "\tskos:notation \"%s\" ;\n", $1
	printf "\n"
	printf "\trdfs:label \"%s\"@cs ;\n", $2
	printf "\t.\n"
	
}