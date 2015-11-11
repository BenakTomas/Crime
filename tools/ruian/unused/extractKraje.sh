#! /bin/sh

baseDir="$1";
inputFilePath="$2";
outputDirectoryPath="$3";
configFilePath="$4";

toolsDirPath="${baseDir}/tools";
ruianExtractorJarPath="${toolsDirPath}/ruianExtractor.jar";

java -jar "${ruianExtractorJarPath}" "${inputFilePath}" "${outputDirectoryPath}" "${configFilePath}";