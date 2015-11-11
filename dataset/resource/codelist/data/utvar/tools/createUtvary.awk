#!/bin/gawk -f
BEGIN {
	FS="#";
}
{
	kr=$1;
	ok=$2;
	ut=$3;
	nazev=$4;
	doLink=$5;
	linkTo=$6;
	
	if(ut == "")
	{
		# kraj nebo okres
		kod=kr ok;
	}
	else
	{
		#utvar
		kod=kr ok ut;
	}
	
	printf "crimestatistics-code-codelist-hash:%s a skos:Concept, crimestatistics-code-codelist-slash:conceptClass ;\n", kod
	printf "\tskos:prefLabel \"%s\"@cs ;\n", nazev
	print	"\tskos:inScheme crimestatistics-code-slash:utvar ;"
	printf "\tskos:notation \"codelist.utvar.%s.%s\" ;\n", year, kod

	printf "\trdfs:label \"%s\"@cs ;\n", nazev
	print "\t."
}