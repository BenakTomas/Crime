#!/bin/sh
solutionRootPath=$4
"${solutionRootPath}"/scripts/extractionScripts/"$2".awk --re-interval  -v codeListName="$1" < "$3"