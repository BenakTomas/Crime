 #!/bin/sh
baseDir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )";
fromDir="$1";

for year in {2008..2012}
do
	"${baseDir}/import.sh" "${fromDir}" $year;
done