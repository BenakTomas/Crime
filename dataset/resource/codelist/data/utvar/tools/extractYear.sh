#! /bin/sh

# nacti si aktualni adresar
baseDir="$1";
toolsDir="${baseDir}/tools";

# nacti vstupni parametry
year=$2;

# nastav promenne
dbName="${year}ku";
baseDirSqlPath="${baseDir}/sql";    #path to sql scripts
yearPath="${baseDir}/${year}";      #path to a directory containing 
extractOkresyFilePath="${baseDirSqlPath}/extractOkresy.sql";  #sql script to extract county level departments
extractRestFilePath="${baseDirSqlPath}/extractRest.sql";
writeResultsFilePath="${baseDirSqlPath}/writeResults.sql";
selectMissingUtvarySqlFilePath="${baseDirSqlPath}/selectMissingUtvary.sql";
okresyDirPath="${yearPath}/okresy";
okresyOutputFilePath="${okresyDirPath}/okresy_actual.csv" ;
okresyMissingFilePath="${baseDir}/okresy_missing.txt";
utvarExtractorFilePath="${toolsDir}/utvarExtractor.exe";
utvaryPath="${yearPath}/utvary";
utvaryXLSSourcePath="${utvaryPath}/source/xls";
utvaryOutputFilePath="${utvaryPath}/utvary_actual.csv";
utvaryExtractionLogPath="${utvaryPath}/utvary_error_log.txt";
utvaryFinalOutputFilePath="${yearPath}/utvary.txt";

# Paths to the resulting CSV files with the code, name and linking information.
# $krajeFinalPath: C:/Documents and Settings/Tom/Dokumenty/crimestatistics/ontology/codelist/data/utvar/$year/kraje/kraje_final.csv
krajeFinalPath="${yearPath}/kraje/kraje_final.csv";
#$centralniUtvaryFinalPath: C:/Documents and Settings/Tom/Dokumenty/crimestatistics/ontology/codelist/data/utvar/$year/centralni_utvary_final.csv
centralniUtvaryFinalPath="${yearPath}/centralni_utvary_final.csv";
# $okresyFinalPath: C:/Documents and Settings/Tom/Dokumenty/crimestatistics/ontology/codelist/data/utvar/$year/okresy/okresy_final.csv
okresyFinalPath="${okresyDirPath}/okresy_final.csv";
# $utvaryToOkresyFinal: C:/Documents and Settings/Tom/Dokumenty/crimestatistics/ontology/codelist/data/utvar/$year/utvary/utvary_toOkresy_final.csv
utvaryToOkresyFinalPath="${utvaryPath}/utvary_toOkresy_final.csv";
# $utvaryToKraje: C:/Documents and Settings/Tom/Dokumenty/crimestatistics/ontology/codelist/data/utvar/$year/utvary/utvary_toKraje_final.csv
utvaryToKrajeFinalPath="${utvaryPath}/utvary_toKraje_final.csv";
# $utvaryToCentralniUtvaryFinalPath: C:/Documents and Settings/Tom/Dokumenty/crimestatistics/ontology/codelist/data/utvar/$year/utvary/utvary_toCentralniUtvary_final.csv
utvaryToCentralniUtvaryFinalPath="${utvaryPath}/utvary_toCentralniUtvary_final.csv";


utvaryTTLOutputFilePath="${yearPath}/ttl";
utvaryDefinitionOutputFilePath="${utvaryTTLOutputFilePath}/utvary_definition.ttl";
utvaryHierarchyOutputFilePath="${utvaryTTLOutputFilePath}//utvary_hierarchy.ttl";
utvarYearDependentTemplatePath="${baseDir}/utvar_template.ttl";
utvaryMissingDataFilePath="${utvaryPath}/utvary_missingData.txt";
okresyMissingDataFilePath="${okresyDirPath}/okresy_missingData.txt";

# extrahuj okresy
echo "Extrahuji okresy...${okresyOutputFilePath}"
rm "${okresyOutputFilePath}" 2> /dev/null;
sed 's/$okresyActualPath/'"$okresyOutputFilePath"'/gI' "${extractOkresyFilePath}" | mysql --user=tom --password=tom123 "${dbName}";
echo "...hotovo."

while true
do
	echo "Zahajuji extrakci utvaru..."
	# Split the resulting county department files so that an extractor app can handle
  # it.
	okresySplitFileMask="${okresyDirPath}/okresy_actual_split_";
	split -l 80 "${okresyOutputFilePath}" "${okresySplitFileMask}";

	# delete output files and log file
	rm "${utvaryOutputFilePath}" "${utvaryExtractionLogPath}"  2> /dev/null;
	
	# Extract department names for the base departments under the county departments
  # specified in ${okresy_splitFilePath}.
  
  # UtvarExtractor produces a log file. If for some county department there was no excel file found, the department's name
  # is listed on a separate line.
  
	for okresy_splitFilePath in "${okresySplitFileMask}"*
	do
		"${utvarExtractorFilePath}" "${okresy_splitFilePath}" "${utvaryXLSSourcePath}" >> "${utvaryOutputFilePath}" 2> "${utvaryExtractionLogPath}";
	done
	
	echo "...hotovo."
  # ${utvaryMissingDataFilePath} is a file where at each line there is a code
  # of a county department for which there is no excel file found. 
  
  # ${okresyMissingDataFilePath} is a file where at each line there is a code
  # of a county department to which there was no name assigned.
  
  # The file ${okresyMissingFilePath} is a global ignore-list containing the
  # codes of county departments for which there is no excel file to be found
  # and no name to be assigned. 
   
	rm "${utvaryMissingDataFilePath}" "${okresyMissingDataFilePath}" 2> /dev/null;

  # Dump the county department codes with missing excel files into ${utvaryMissingDataFilePath}. 	 
  sed -nr '/^a[0-9]{4}__\.xls$/s/^a([0-9]{4})__\.xls$/\1/gIp' "${utvaryExtractionLogPath}" | xargs -I {} bash -c 'if ! grep -Fq {} "'"${okresyMissingFilePath}"'"; then echo {} >> "'"${utvaryMissingDataFilePath}"'"; fi';
	# Dump the county department codes with missing names into ${okresyMissingDataFilePath}.
  sed -nr '/nezn/s/^.*nezn[^0-9]*([0-9]{4})#.*$/\1/gIp' "${okresyOutputFilePath}" | xargs -I {} bash -c 'if ! grep -Fq {} "'"${okresyMissingFilePath}"'"; then echo {} >> "'"${okresyMissingDataFilePath}"'"; fi';
	# Open the files for the user to view them.
  pspad "${utvaryMissingDataFilePath}" "${okresyMissingDataFilePath}";
	
  # Ask the user whether to perform the base department name extraction cycle
  # once more.
  # Typically the user viewed the result files ${utvaryMissingDataFilePath} and
  # ${okresyMissingDataFilePath} and added missing excel files and filled in
  # missing county department names, so these files are going to get smaller
  # after the next iteration. When the user is satisfied with the amount of
  # extracted information, he or she enters 'n' to break out of the extraction
  # cycle. 
  echo "Extrahovat utvary znovu? (y/n)";
	read continueYesNo;
	
	if [ $continueYesNo == "n" ]
	then
		break;
	else
		echo "Yesno: '${continueYesNo}'";
	fi;
done;

echo "Vytvarim utvary podle extrahovanych dat..."

# Delete all *final.csv
find "${yearPath}" -name *final.csv |
xargs -I finalFile rm finalFile 2> /dev/null;

rm "${utvaryFinalOutputFilePath}" 2> /dev/null;
                                             
# Run the SQL script that extracts the base department names and codes.
#C:/Documents and Settings/Tom/Dokumenty/crimestatistics/ontology/codelist/data/utvar/$year/utvary/utvary_actual.csv
sed 's/$okresyActualPath/'"${okresyOutputFilePath}"'/gI;s/$utvaryActualPath/'"${utvaryOutputFilePath}"'/gI' "${extractRestFilePath}" | mysql --user=tom --password=tom123 "${dbName}";
echo "...hotovo."

# Pripoj se k DB a nechej si vypsat pripadne chybejici utvary.
# Run the SQL script that selects the base departments that have not
# been processed, i.e. assigned a name.
echo "Zjistuji, jestli nektere utvary nechybeji...";
mysql --user=tom --password=tom123 "${dbName}" < "${selectMissingUtvarySqlFilePath}";
echo "...hotovo."

# If the user is satisfied with the extracted results, he or she
# is offerred that the results are written into a set of files.
echo "Write down the results? (y/n)";
read doWriteResults;

if [ $doWriteResults == "y" ]
then
	# Vysledky zapis do souboru
  # s/$/'"${}"'/gI
	sed 's/$krajeFinalPath/'"${krajeFinalPath}"'/gI; s/$centralniUtvaryFinalPath/'"${centralniUtvaryFinalPath}"'/gI; s/$okresyFinalPath/'"${okresyFinalPath}"'/gI; s/$utvaryToOkresyFinalPath/'"${utvaryToOkresyFinalPath}"'/gI; s/$utvaryToKrajeFinalPath/'"${utvaryToKrajeFinalPath}"'/gI; s/$utvaryToCentralniUtvaryFinalPath/'"${utvaryToCentralniUtvaryFinalPath}"'/gI' "${writeResultsFilePath}" | mysql --user=tom --password=tom123 "${dbName}";
	echo "Vysledky zapsany.";
else
	echo "Nektere utvary nebyly spravne extrahovany!";
	exit 1;
fi;

# Vysledky spoj do jedineho souboru
# Merge the *final.csv result files into a single result
# '${utvaryFinalOutputFilePath}' file.
find "$yearPath" -name *final.csv -print0 |
xargs -0 -I file cat file >> "${utvaryFinalOutputFilePath}";

# Vytvor soubor s definicemi utvaru.
# Run the script that creates the departments definitions (skos:Concept instances).
# A template file '${utvarYearDependentTemplatePath}' is used for a result file
# header.
# A 'createUtvary.awk' script does the extraction work, using the $year
# as a parameter to insert the created departments in a correct namespace
# for the given year.  
sed 's/year/'"$year"'/gI' "${utvarYearDependentTemplatePath}" > "${utvaryDefinitionOutputFilePath}";
echo "" >> "${utvaryDefinitionOutputFilePath}";
echo "" >> "${utvaryDefinitionOutputFilePath}"; 
"${toolsDir}/createUtvary.awk" -v year="${year}" < "${utvaryFinalOutputFilePath}" >> "${utvaryDefinitionOutputFilePath}";

# Vytvor soubor s definici hierarchie utvaru.
# A script is run that writes down the department hierarchy information
# using the information in the final result file. It either links automatically
# to parent departments using the department code hierarchy, or links
# departments to explicitly set parents.
sed 's/year/'"$year"'/gI' "${utvarYearDependentTemplatePath}" > "${utvaryHierarchyOutputFilePath}";
echo "" >> "${utvaryHierarchyOutputFilePath}";
echo "" >> "${utvaryHierarchyOutputFilePath}";
"${toolsDir}/linkUtvary.awk" < "${utvaryFinalOutputFilePath}" >> "${utvaryHierarchyOutputFilePath}";

echo "Utvary byly uspesne extrahovany.";