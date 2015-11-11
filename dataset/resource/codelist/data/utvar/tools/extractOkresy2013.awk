#!/bin/gawk -f
BEGIN {
	FS="#";
}
{
	kr=substr($1, 1, 2);
	ok=substr($1, 3, 2);
	
	if(ok=="00")
	{
		next;
	}
	
	nazev=$2;
	
	printf "INSERT INTO okresy_actual(kr, ok, nazev) VALUES('%s','%s','%s'); \n", kr, ok, nazev
}