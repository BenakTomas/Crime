#!/bin/gawk -f
BEGIN{
	FS="#"
}
/^[0-9]{4,6}/{
	for(i=1; i <= NF; ++i)
	{
		sub(/^[[:space:]]*/, "", $i)
		sub(/[[:space:]]*$/, "", $i)
	}
	
	printf "crimestatistics-code-codelist-hash:%s a skos:Concept, crimestatistics-code-codelist-slash:conceptClass ;\n", $1
	printf "\tskos:prefLabel \"%s\"@cs ;\n", $2
	printf "\tskos:inScheme crimestatistics-code-codelist-slash:%s ;\n", codeListName
	printf "\tskos:notation \"%s\" ;\n", $1
	printf "\n"
	printf "\trdfs:label \"%s\"@cs ;\n", $2
	printf "\n"
	printf "\tcrimestatistics-code-codelist-property:uzemi \"%s\"^^xsd:boolean ;\n", ($3 == "A" ? "true" : "false")
	printf "\tcrimestatistics-code-codelist-property:sledovaneMisto \"%s\"^^xsd:boolean ;\n", ($4 == "A" ? "true" : "false")
	printf "\t.\n"
}