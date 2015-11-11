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

while read file
do
	fileSize=`wc -c "${file}" | cut -d' ' -f1;`
	
	if [ $[$currentJointXmlFileSize + $fileSize] -ge $effectiveMaxJointXmlFileSize ]
	then
		echo "Closing the current xml file (${currentJointXmlFilePath}) due to its size...";
		# insert end root element into an old xml file
		echo "</root>" >> "${currentJointXmlFilePath}";
		
		currentJointXmlFileNumber=$[$currentJointXmlFileNumber + 1];
		currentJointXmlFilePath="${outputDirPath}/${jointXmlFileNameTemplate}_${currentJointXmlFileNumber}.xml";
		
		echo "Starting a new xml file (${currentJointXmlFilePath})..."
		# insert start root element into new file
		echo "<root>" > "${currentJointXmlFilePath}";
		currentJointXmlFileSize=$startRootElementByteSize;
	fi
	
	currentJointXmlFileSize=$[$currentJointXmlFileSize + $fileSize];
	
	cat "${file}" >> "${currentJointXmlFilePath}";
done <<EOT
$(ls "${sourceDirPath}/"*.xml -1)
EOT

echo "Closing the last xml file (${currentJointXmlFilePath}).";
# insert end root element into the last xml file
echo "</root>" >> "${currentJointXmlFilePath}";