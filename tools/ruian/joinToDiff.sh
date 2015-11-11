#! /bin/sh

maxJointXmlFileSize=$1;
sourceDirPath="$2";
outputDirPath="$3";

currentJointXmlFileSize=0;
currentJointXmlFileNumber=1;
startRootElementByteSize=`echo "<root>" | wc -c`;
endRootElementByteSize=`echo "</root>" | wc -c`;
effectiveMaxJointXmlFileSize=$[$maxJointXmlFileSize - $endRootElementByteSize];

jointXmlFileNameTemplate="obce";
currentJointXmlFilePath="${outputDirPath}/${jointXmlFileNameTemplate}_${currentJointXmlFileNumber}.xml";

# insert start root element into first xml file
echo "Starting first xml file (${currentJointXmlFilePath}).";
echo "<root>" > "${currentJointXmlFilePath}";

currentJointXmlFileSize=$startRootElementByteSize;

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