#!/bin/gawk -f
BEGIN {
	FS="#"
}
{
	kr=$1;
	ok=$2;
	ut=$3;
	nazev=$4;
	
	printf "INSERT INTO utvary_actual(kr, ok, ut, nazev) VALUES('%s','%s','%s','%s'); \n", kr, ok, ut, nazev;
}