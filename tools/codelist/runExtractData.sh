#!/bin/sh
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
"./scripts/general/extractCodelistsData.awk" -v solutionRootPath="$DIR" < ./config/config_data