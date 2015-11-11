#!/bin/gawk -f
BEGIN {
	FS="#";
}
{
  kod=$1;
	kr=substr($1, 1, 2);
	ok=substr($1, 3, 2);
	ut=substr($1, 5, 2);
	nazev=$2;
	
	printf "crimestatistics-code-codelist-hash:%s a skos:Concept, crimestatistics-code-codelist-class:conceptClass ;\n", kod;
	printf "\tskos:prefLabel \"%s\"@cs ;\n", nazev;
	print	"\tskos:inScheme crimestatistics-code-slash:utvar ;";
	printf "\tskos:notation \"codelist.utvar.%s\" ;\n", kod;
  printf "\tcrimestatistics-code-codelist-property:id \"%s\" ;\n", kod;

	printf "\trdfs:label \"%s\"@cs ;\n", nazev;
	print "\t.";
}