#! /bin/sh

entityType="$1"
workingDir="$2"
TOOLS_DIR="${CRIME_HOME}/tools";

"${TOOLS_DIR}/dumprdf_Basic.sh" "${entityType}" "${workingDir}"
"${TOOLS_DIR}/dumprdf_Versions.sh" "${entityType}" "${workingDir}"