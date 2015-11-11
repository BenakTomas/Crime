#!/bin/gawk -f
BEGIN {
	FS="#";
}
{
	kr=$1;
	ok="00";
	ut="";
	nazev=$2;
	doLink=0;
	linkTo="";
	
	printf "%s#%s#%s#%s#%d#%s\n", kr, ok, ut, nazev, doLink, linkTo
}