#!/bin/gawk -f
BEGIN {
	FS="#";
}
{
	kr=$1;
	ok=$2;
	ut="";
	nazev=$3;
	doLink=1;
	linkTo="";
	
	printf "%s#%s#%s#%s#%d#%s\n", kr, ok, ut, nazev, doLink, linkTo
}