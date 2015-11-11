#!/bin/awk -f
BEGIN{
	FS="#"
}
{
	system(sprintf("\"%s\"/scripts/general/extractCodelistsData.sh \"%s\" \"%s\" \"%s\"", solutionRootPath, $1, $2, solutionRootPath))
}