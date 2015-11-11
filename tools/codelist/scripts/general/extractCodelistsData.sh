#!/bin/sh

codeListName=$1
usesCustomImportScript=$2
solutionRootPath=$3
headerFilePath="${solutionRootPath}/headers/codelistHeader.ttl"
sourceFilePath="${solutionRootPath}/source/${codeListName}.csv"
outputDataTTLFilePath="${solutionRootPath}/data/${codeListName}_data.ttl"

cp "$headerFilePath" "$outputDataTTLFilePath"

echo "" >> "$outputDataTTLFilePath"
echo "" >> "$outputDataTTLFilePath"

echo "#Definice členských konceptů" >> "$outputDataTTLFilePath"
if [ $usesCustomImportScript -eq 1 ]
then
	"${solutionRootPath}"/scripts/general/extractCodeListConcepts.sh "$codeListName" "$codeListName" "$sourceFilePath" "${solutionRootPath}" >> "$outputDataTTLFilePath"
else
	"${solutionRootPath}"/scripts/general/extractCodeListConcepts.sh "$codeListName" "extractCodeListConceptsGeneric" "$sourceFilePath" "${solutionRootPath}">> "$outputDataTTLFilePath"
fi