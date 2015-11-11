#!/bin/sh
# get the current directory
dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
# set up the variables
solutionRootPath=$dir
codelistName="$1"
sourceFileName="$1.csv"
headerFileName="codeListHeader.ttl.template"
outFileName="$1_data.ttl"
if [ $# -eq 1 ]; then
	scriptFileName="extractCodelistConceptsGeneric"
else
	scriptFileName="$2"
fi

"${solutionRootPath}/extractFlatDataCustom.sh" "${codelistName}" "${sourceFileName}" "${headerFileName}" "${scriptFileName}" "${outFileName}" 