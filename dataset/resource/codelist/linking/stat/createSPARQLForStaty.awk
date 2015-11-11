#!/bin/gawk -f
BEGIN {
	FS="#";
}
{
	kod=$1;
	numKeywords=split($2, keywords, " ");
	
	printf "UNION { ?country a <http://dbpedia.org/ontology/Country>; <http://dbpedia.org/ontology/capital> ?capital . OPTIONAL {?country dbpprop:yearEnd ?yearEnd } FILTER(!bound(?yearEnd)) . ?country rdfs:label ?label . "; 
	
	for(i=1; i <= numKeywords; i++)
	{
		printf "FILTER(REGEX(?label, \"%s\")) ", keywords[i];
	}
	
	printf "BIND(<http://linked.opendata.cz/ontology/crimestatistics/code/stat#%s> as ?stat) }\n", kod
}