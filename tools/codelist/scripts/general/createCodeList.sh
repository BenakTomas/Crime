#!/bin/sh

codeListName=$1
usesCustomImportScript=$2
solutionRootPath=$3
inputSemanticFilePath="${solutionRootPath}/semantics/${codeListName}.ttl"
outputTTLFilePath="${solutionRootPath}/result/${codeListName}.ttl"
sourceFilePath="${solutionRootPath}/source/${codeListName}.csv"

cp "$inputSemanticFilePath" "$outputTTLFilePath"

echo "" >> "$outputTTLFilePath"
echo "" >> "$outputTTLFilePath"

echo "#Definice členských konceptů" >> "$outputTTLFilePath"
if [ $usesCustomImportScript -eq 1 ]
then
	"${solutionRootPath}"/scripts/general/extractCodeListConcepts.sh "$codeListName" "$codeListName" "$sourceFilePath" "${solutionRootPath}" >> "$outputTTLFilePath"
else
	"${solutionRootPath}"/scripts/general/extractCodeListConcepts.sh "$codeListName" "extractCodeListConceptsGeneric" "$sourceFilePath" "${solutionRootPath}">> "$outputTTLFilePath"
fi