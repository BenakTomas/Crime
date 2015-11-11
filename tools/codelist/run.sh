#!/bin/sh
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
"./scripts/general/createCodeLists.awk" -v solutionRootPath="$DIR" < ./config/config