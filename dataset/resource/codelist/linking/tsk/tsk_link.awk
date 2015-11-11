#!/bin/gawk -f
BEGIN {
	FS="#";
}
{
	tsk=$1;
	lawReferencesSection=$3;
	
	if(lawReferencesSection ~ /^[[:space:]]*$/) next;
	
	numberOfReferences=split(lawReferencesSection, lawReferences, ",");
	
	if(numberOfReferences == 0) next;

	printf "<http://linked.opendata.cz/ontology/crimestatistics/code/tsk/%s#%s> ", trestniZakon, tsk;
	
	if(numberOfReferences == 1)
	{
		lawReference = lawReferences[1];
    sub(/^[[:space:]]*/, "", lawReference);
		sub(/[[:space:]]*$/, "", lawReference);
		printf "<http://linked.opendata.cz/ontology/crimestatistics/property/pravniKvalifikaceTsk> <http://linked.opendata.cz/resource/legislation/cz/act/%s/section/%s> .\n", trestniZakon, lawReference;
	}
	else
	{
		lawReference=lawReferences[1];
		sub(/^[[:space:]]*/, "", lawReference);
		sub(/[[:space:]]*$/, "", lawReference);
		
		printf "<http://linked.opendata.cz/ontology/crimestatistics/property/pravniKvalifikaceTsk> \n";
		printf "<http://linked.opendata.cz/resource/legislation/cz/act/%s/section/%s>", trestniZakon, lawReference;
		
		for(i=2; i <= numberOfReferences; i++)
		{
			lawReference=lawReferences[i];
			sub(/^[[:space:]]*/, "", lawReference);
			sub(/[[:space:]]*$/, "", lawReference);
			
			printf ",\n";
			printf "<http://linked.opendata.cz/resource/legislation/cz/act/%s/section/%s>", trestniZakon, lawReference;
		}
		
		printf " .\n"
	}
}