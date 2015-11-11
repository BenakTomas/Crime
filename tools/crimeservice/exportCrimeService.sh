#! /bin/sh


rm -r "/c/crimestatistics/crimeservice/deploy/"* "/c/crimestatistics/crimeservice/deploy/.classpath" "/c/crimestatistics/crimeservice/deploy/.project" 2> /dev/null
svn export --force "C:\Documents and Settings\Tom\workspace\CrimeService" "/c/crimestatistics/crimeservice/deploy"

makewardefault.sh