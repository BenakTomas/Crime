#! /bin/sh

baseDir="$1";
obceDirPath="${baseDir}/obce";
sourceDirPath="${obceDirPath}/source";
extractTempDirPath="${sourceDirPath}/extracted";
templatesDirPath="${baseDir}/templates";
namespacesDefinitionFilePath="${templatesDirPath}/namespaces.txt";
toolsDirPath="${baseDir}/tools";

extractedXmlFileNameTemplate="obce_extracted_";
extractedXmlFilePath="${extractTempDirPath}/${extractedXmlFileNameTemplate}.xml";
extractedXmlFilePathTemplate="${extractTempDirPath}/obce_extracted";
extractedTempXmlFilePathTemplate="${extractTempDirPath}/temp_";

#echo "" >> "${extractedXmlFilePath}";

currentXmlFileSize=0;
currentXmlFileNumber=1;
maxXmlFileSize=50000000;
startRootElementByteSize=`echo "<root>" | wc -c`;
endRootElementByteSize=`echo "</root>" | wc -c`;
effectiveMaxXmlFileSize=$[$maxXmlFileSize - $endRootElementByteSize];
tempFileCounter=0;

currentXmlFilePath="${extractedXmlFilePathTemplate}_${currentXmlFileNumber}.xml";
ruianExtractorJarFilePath="${toolsDirPath}/ruianExtractor.jar";
# projdi kazdy .gz soubor, rozbal jej, extrahuj data pro uzemni jednotky do zvlastnich adresaru

# inicializuj soubory s extrahovanymi daty

echo "Initializing output files for uzemni jednotky..."

#echo "<root>" > "${extractTempDirPath}/obce.xml";
#echo "<root>" > "${extractTempDirPath}/castiObci.xml";
#echo "<root>" > "${extractTempDirPath}/katastralniUzemi.xml";
#echo "<root>" > "${extractTempDirPath}/zsj.xml";

echo "Starting extraction...";
echo "Starting the first xml file...";

# insert start root element into first xml file
echo "<root>" > "${currentXmlFilePath}";

find "${sourceDirPath}" -name '*.gz' -print |
while read file
do
	extractedTempXmlFilePath="${extractedTempXmlFilePathTemplate}_${tempFileCounter}.xml";
	
	gzip -cd "${file}" |
	tail -n +2 > "${extractedTempXmlFilePath}";
	
	tempFileSize=`wc -c "${extractedTempXmlFilePath}" | cut -d' ' -f1;`
	
	if [ $[$currentXmlFileSize + $tempFileSize] -ge $effectiveMaxXmlFileSize ]
	then
		echo "Closing the current xml file due to its size...";
		# insert end root element into an old xml file
		echo "</root>" >> "${currentXmlFilePath}";
		
		#echo "Extracting uzemni jednotky from the current xml file...";
		# extrahuj data obci
		#java -jar "${ruianExtractorJarFilePath}" "${currentXmlFilePath}" "${extractTempDirPath}";
		
		#echo "...done";
		
		# odstran jiz nepotrebny soubor
		#rm "${currentXmlFilePath}";
		
		currentXmlFileNumber=$[$currentXmlFileNumber + 1];
		currentXmlFilePath="${extractedXmlFilePathTemplate}_${currentXmlFileNumber}.xml";
		
		echo "Starting a new xml file..."
		# insert start root element into new file
		echo "<root>" > "${currentXmlFilePath}";
		currentXmlFileSize=$startRootElementByteSize
	else
		currentXmlFileSize=$[$currentXmlFileSize + $tempFileSize];
	fi
	
	cat "${extractedTempXmlFilePath}" >> "${currentXmlFilePath}";
	
	rm "${extractedTempXmlFilePath}";
	tempFileCounter=$[$tempFileCounter + 1];
	#echo "" >> "${extractedXmlFilePath}";
done

echo "Closing the last xml file...";
# insert end root element into the last xml file
echo "</root>" >> "${currentXmlFilePath}";

#echo "Extracting uzemni jednotky from the last xml file...";
# extrahuj data obci z posledniho souboru
#java -jar "${ruianExtractorJarFilePath}" "${currentXmlFilePath}" "${extractTempDirPath}";
#echo "...done";
# smaz posledni soubor a posledni docasny soubor
#rm "${extractedTempXmlFilePath}";

# ukonci soubory s extrahovanymi daty
#echo "</root>" >> "${extractTempDirPath}/obce.xml";
#echo "</root>" >> "${extractTempDirPath}/castiObci.xml";
#echo "</root>" >> "${extractTempDirPath}/katastralniUzemi.xml";
#echo "</root>" >> "${extractTempDirPath}/zsj.xml";
#echo "</root>" >> "${extractTempDirPath}/zsj.xml";