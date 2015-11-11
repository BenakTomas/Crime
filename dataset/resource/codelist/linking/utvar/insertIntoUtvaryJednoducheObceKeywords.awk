#!/bin/gawk -f
BEGIN {
	FS="#";
	
	print "USE crimestatistics;";
	print "TRUNCATE TABLE utvary_keywords_jednoducheObce;"
}
{
	kr=$1;
  ok=$2;
  ut=$3;
	labelsCount = split($4, labels, " ");
	
	for(i=1; i<=labelsCount; i++)
	{
		printf "INSERT INTO utvary_keywords_jednoducheObce(kr, ok, ut, keyword) VALUES('%s', '%s', '%s', '%s');\n", kr, ok, ut, labels[i];
	}
}