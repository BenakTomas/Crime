#! /bin/sh

GetXmlFilePath () {
   echo "${1}/extracted_${2}.xml";
}

FormatRuianFileNameFromDate () {
   echo "http://vdp.cuzk.cz/vymenny_format/soucasna/${1}_ST_ZKSH.xml.gz";
}   

Log () {
  echo "$1" >> "$2";
}
baseDir="${CRIME_HOME}";
toolsDir="${baseDir}/tools";
ruianDir="${baseDir}/ruian";

dateFrom="$1";
dateTo="$2";

workingDir="${ruianDir}/`date +%s`";
mkdir "${workingDir}"

extractedDir="${workingDir}/extracted";
mkdir "${extractedDir}"


currentMergedXmlFileSize=0;
currentMergedXmlFileNumber=1;
maxXmlFileSize=4294967296;
startRootElementByteSize=`echo "<root>" | wc -c`;
endRootElementByteSize=`echo "</root>" | wc -c`;
effectiveMaxXmlFileSize=$[$maxXmlFileSize - $endRootElementByteSize];

downloadedZipFileName="${workingDir}/zippedRuianFile.gz";
unzippedXmlFilePath="${workingDir}/ruianFile.xml";

logPath="${workingDir}/log.log";
errorLogPath="${workingDir}/error.log";
wgetLogPath="${workingDir}/download.log";

currentMergedXmlFilePath=`GetXmlFilePath "${workingDir}" "${currentMergedXmlFileNumber}"`
Log "Current merged file is <${currentMergedXmlFilePath}>." "${logPath}"

# vygeneruj seznam datumu
# stahni soubory pomoci wget
while read fileDate
do
  fileUrl=`FormatRuianFileNameFromDate "${fileDate}"`
  Log "Downloading file <$downloadedZipFileName> from URL <$fileUrl>..." "${logPath}"
  # stahni soubor
  wget -O "${downloadedZipFileName}" -o "${wgetLogPath}" -c -t 1 "${fileUrl}"
  
  if [ $? -ne 0 ]
  then
    if grep 'ERROR 404' "${wgetLogPath}"
    then
      continue
    else
      echo "Chyba pri stahovani."
      Log "Error when trying to download file <${fileUrl}>. Aborting operation."
      rm "${downloadedZipFileName}" "${currentMergedXmlFilePath}"
      exit
    fi
  fi
  
    Log "done." "${logPath}"
    Log "Unzipping file to <$unzippedXmlFilePath>." "${logPath}"
    gzip -cd "${downloadedZipFileName}" |
    tail -n +2 > "${unzippedXmlFilePath}";
    Log "done." "${logPath}"
    # smaz .gz soubor
    rm "${downloadedZipFileName}"
    
    # pripoj ho k aktualnimu velkemu souboru XML
    tempFileSize=`wc -c "${unzippedXmlFilePath}" | cut -d' ' -f1;`
    if [ $[$currentMergedXmlFileSize + $tempFileSize] -ge $effectiveMaxXmlFileSize ]
    then
      echo "Finishing file <${currentMergedXmlFilePath}>" "${logPath}"
    	echo "</root>" >> "${currentMergedXmlFilePath}";
      
      # extrahuj uzemni jednotky
      "${toolsDir}/extractUzemniJednotky.sh" "${baseDir}" "${workingDir}" "${extractedDir}" "${toolsDir}/config/ruianExtractorConfig_obce.cfg" 
    	
    	currentMergedXmlFileNumber=$[$currentMergedXmlFileNumber + 1];
      
      rm "${currentMergedXmlFilePath}"
      
      Log "done." "${logPath}" 
    	currentMergedXmlFilePath=`GetXmlFilePath "${workingDir}" "${currentMergedXmlFileNumber}"`
      Log "Current merged file is <${currentMergedXmlFilePath}>." "${logPath}"
    	
    	echo "<root>" > "${currentMergedXmlFilePath}";
    	currentMergedXmlFileSize=$startRootElementByteSize
    else
    	currentMergedXmlFileSize=$[$currentMergedXmlFileSize + $tempFileSize];
    fi
    
    Log "Merging <${unzippedXmlFilePath}> to current merged file." "${logPath}"
    cat "${unzippedXmlFilePath}" >> "${currentMergedXmlFilePath}";
    # smaz docasny rozbaleny XML soubor
    rm "${unzippedXmlFilePath}";
    Log "done." "${logPath}"
  
done<<EOT
$("${toolsDir}/genDateRange.sh" "${dateFrom}" "${dateTo}")
EOT

echo "</root>" >> "${currentMergedXmlFilePath}";

Log "${currentMergedXmlFileSize}" "${logPath}"
if [ $currentMergedXmlFileSize -ge 0 ]
then
# extrahuj uzemni jednotky
Log "Extracting data" "${logPath}"
"${toolsDir}/extractUzemniJednotky.sh" "${baseDir}" "${workingDir}" "${extractedDir}" "${toolsDir}/config/ruianExtractorConfig_obce.cfg"
fi 

find "${extractedDir}" -name "*.xml" -print |
while read extractedFilePath
do
  inputType=`basename "${extractedFilePath}.xml"`
  "${toolsDir}/transformFromXmlToSql.sh" "${inputType}" "${inputType}" "kod" "${extractedFilePath}" "${workingDir}";
done

# TODO spustit SQL soubory
find "${extractedDir}" -name "*.sql" -print |
while read sqlScriptFilePath
do
  "${toolsDir}/runSQLScript.sh" "${logPath}" "${errorLogPath}"
done

echo "${workingDir}";
