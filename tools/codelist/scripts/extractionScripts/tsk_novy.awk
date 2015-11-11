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
		
		kod=$1;
		popisek=$2;
		
		printf "crimestatistics-code-codelist-hash:%s a skos:Concept, crimestatistics-code-codelist-slash:conceptClass ;\n", kod
		printf "\tskos:prefLabel \"%s\"@cs ;\n", popisek
		printf "\tskos:inScheme crimestatistics-code-slash:%s ;\n", codeListName
		printf "\tskos:notation \"%s.%s.%s\" ;\n", codeListName, "novy", kod
		printf "\n"
		printf "\trdfs:label \"%s\"@cs ;\n", popisek
		printf "\t.\n"
	}
}