#!/bin/gawk -f
BEGIN {
	FS="#";
}
{
	kr=$1;
	ok=$2;
	nazev=$3;
	doLink=1;
	linkTo="";
	
	printf "INSERT INTO 2008ku.okresy_actual(`kr`,`ok`,`nazev`,`doLink`,`linkTo`) VALUES ('%s', '%s', '%s', %d, '%s');\n", kr, ok, nazev, doLink, linkTo
}