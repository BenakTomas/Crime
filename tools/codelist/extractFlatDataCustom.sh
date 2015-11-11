#!/bin/sh

# set up the variables
codeListName=$1;
sourceFileName=$2
headerFileName=$3;
scriptFileName=$4;
outFileName=$5;

# get the current directory and set is as the solution root directory: all above fileNames relate to the solution root directory to form an absolute path
solutionRootPath="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )";

# set absolute paths to files used
sourceFilePath="${solutionRootPath}/source/${sourceFileName}";
headerFilePath="${solutionRootPath}/headers/${headerFileName}";
scriptFilePath="${solutionRootPath}/scripts/extractionScripts/${scriptFileName}";
outFilePath="${solutionRootPath}/data/${outFileName}";

# prepare header
cp "${headerFilePath}" "${outFilePath}"

# insert two line breaks to increase readability
echo "" >> "${outFilePath}"
echo "" >> "${outFilePath}"

# run extraction
"${scriptFilePath}" --re-interval  -v codeListName="$codeListName" < "${sourceFilePath}" >> "${outFilePath}"