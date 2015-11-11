#!/bin/gawk -f
BEGIN {
	FS="#";
}
{
	kr=$1;
	kod=kr "00"
	nazev=$4;
	concept=sprintf("crimestatistics-code-codelist-hash:%s", kod);
	
	printf "UNION { ?kraj rdf:type cm:Region . ?kraj geonames:officialName ?name . FILTER(REGEX(STR(?name),\"%s\")) BIND(%s as ?kod) }\n", nazev, concept;
}