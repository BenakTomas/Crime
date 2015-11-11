#!/bin/awk -f
BEGIN{
	FS="#"
}
{
	system(sprintf("\"%s\"/scripts/general/createCodeList.sh \"%s\" \"%s\" \"%s\"", solutionRootPath, $1, $2, solutionRootPath))
}