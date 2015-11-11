#!/bin/gawk -f
BEGIN {
	FS="#";
}
{
	kr=substr($1, 1, 2);
	ok=substr($1, 3, 2);
	ut=substr($1, 5, 2);
	nazev=$2;
	
	printf "INSERT INTO utvary_actual(kr, ok, ut, nazev) VALUES('%s','%s','%s','%s'); \n", kr, ok, ut, nazev
}