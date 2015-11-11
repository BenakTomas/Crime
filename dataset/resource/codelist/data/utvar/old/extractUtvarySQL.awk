#!/bin/gawk -f
BEGIN {
	FS="#";
}
{
	kr=$1;
	ok=$2;
	ut=$3
	nazev=$4;
	doLink=1;
	linkTo="";
	
	printf "INSERT INTO 2008ku.utvary_actual(`kr`,`ok`,`ut`,`nazev`,`doLink`,`linkTo`) VALUES ('%s', '%s', '%s', '%s', %d, '%s');\n", kr, ok, ut, nazev, doLink, linkTo
}