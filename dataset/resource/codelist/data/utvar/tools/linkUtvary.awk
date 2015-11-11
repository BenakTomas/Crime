#!/bin/gawk -f
BEGIN {
	FS="#";
}
{
	kr=$1;
	ok=$2;
	ut=$3
	nazev=$4;
	doLink=$5;
	linkTo=$6;
	
	if(doLink == 1)
	{
		if(ok == "")
		{
			# kraj - přidám "00"
			kod=kr "00";
		} else if(ut == "")
		{
			# okres
			kod=kr ok;
		}
		else
		{
			#utvar
			kod=kr ok ut;
		}
		
		printf "crimestatistics-code-codelist-hash:%s\n", kod
	
		if(linkTo != "")
		{
			printf "\tskos:broader crimestatistics-code-codelist-hash:%s ;\n", linkTo
		}
		else
		{
			if(length(kod) == 6)
			{
				# linkování útvaru na okres: Useknu poslední dva znaky.
				linkTo=substr(kod, 1, 4);
			}
			else if(length(kod) == 4)
			{
				# linkování okresu na kraj: Useknu poslední dva znaky a přidám "00".
				linkTo=substr(kod, 1, 2) "00";
			}
			
			printf "\tskos:broader crimestatistics-code-codelist-hash:%s ;\n", linkTo;
		}
		
		print "\t."
	}
	
}