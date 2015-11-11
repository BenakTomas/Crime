#!/bin/gawk -f
BEGIN {
	FS="#";
	
	print "USE crimestatistics;";
	print "TRUNCATE TABLE staty_keywords;"
}
{
	kod=$1;
	labelsCount = split($2, labels, " ");
	
	for(i=1; i<=labelsCount; i++)
	{
		printf "INSERT INTO staty_keywords(kod, keyword) VALUES('%s', '%s');\n", kod, labels[i];
	}
}