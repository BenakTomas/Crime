#!/bin/gawk -f
BEGIN {
	FS="#";
}
{
	kr=$1;
	ok=$2;
	nazev=$3;
	kod=kr ok;
	concept=sprintf("crimestatistics-code-codelist-hash:%s", kod);
	
	printf "UNION { ?okres rdf:type cm:County . ?okres geonames:officialName ?name . FILTER(STR(?name) = \"%s\") BIND(%s as ?kod) }\n", nazev, concept;
}