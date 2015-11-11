#! /bin/sh
validFrom="$1"
inputFilePath="$2"

# priprav si pracovni adresar
baseDir="${CRIME_HOME}"
workingDir="${baseDir}/extract/utvary/`date +%s`";
mkdir "${workingDir}"

toolsDir="${baseDir}/tools";

# transformuj vstupni soubor na SQL a spust nad databazi
"${toolsDir}/transformFromXmlToSql.sh" "utvary" "${validFrom}" "utvar_geom" "ut" "${inputFilePath}" "${workingDir}"

# TODO extrahuj data pomoci d2rq
"${toolsDir}/dumprdf_all.sh" "utvar" "${workingDir}"
# TODO spust SPARQL update skripty
"${toolsDir}/updateUtvary_sparql.sh
