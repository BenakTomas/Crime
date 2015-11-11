#!/bin/gawk -f
BEGIN{
	FS="#"
}
/^[1-9][0-9][0-9]/{
	if(NR > 1)
	{
		for(i=1; i <= NF; ++i)
		{
			sub(/^[[:space:]]*/, "", $i)
			sub(/[[:space:]]*$/, "", $i)
		}

		printf "crimestatistics-code-codelist-hash:%s a skos:Concept, crimestatistics-code-codelist-slash:conceptClass ;\n", $1
		printf "\tskos:prefLabel \"%s\"@cs ;\n", $2
		printf "\tskos:inScheme crimestatistics-code-codelist-slash:%s ;\n", codeListName
		printf "\tskos:notation \"%s\" ;\n", $1
		printf "\tskos:note \"%s\"@cs ;\n", $3
		printf "\n"
		printf "\trdfs:label \"%s\"@cs ;\n", $2
		printf "\t.\n"
	}
}