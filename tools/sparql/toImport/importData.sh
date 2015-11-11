 #!/bin/sh
baseDir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )";
fromDir="$1";

if [ $# -eq 2 ]
then
	year=$2;
	dataDir="${baseDir}/${year}/data";
	extractAllYears=0;
else
	dataDir="${baseDir}/data";
	extractAllYears=1;
fi

if [ $extractAllYears -eq 0 ]
then
	startYear=$year;
	endYear=$startYear;
else
	startYear=2008;
	endYear=2012;
fi

rm "${dataDir}/"*.ttl 2> /dev/null;
rm "${dataDir}/"*.n3 2> /dev/null;

for((extractYear=$startYear;extractYear<=$endYear;extractYear++))
do
	datasetDataDirPath="${fromDir}/resource/dataset/data/${extractYear}";
	find "${datasetDataDirPath}" \( -name '*.ttl' -o -name '*.n3' \) -print0 | xargs -0 -I file cp file "${dataDir}";
done