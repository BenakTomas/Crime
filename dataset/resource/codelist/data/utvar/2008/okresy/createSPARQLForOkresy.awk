#!/bin/gawk -f
BEGIN {
	FS="#";
}
{
	kr=$1;
	ok=$2;
	nazev=$4;
	kod=kr ok;
	concept=sprintf("crimestatistics-code-codelist-hash:%s", kod);
	
	printf "UNION { ?okres rdf:type cm:County . ?okres geonames:officialName ?name . FILTER(REGEX(?name, \"%s\")) BIND(%s as ?kod) }\n", nazev, concept;
}