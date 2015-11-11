#! /bin/sh

baseDir="$1";
inputFilePath="$2";
outputDirPath="$3";
configFilePath="$4";

toolsDirPath="${baseDir}/tools";
ruianExtractorJarFilePath="${toolsDirPath}/ruianExtractor.jar";

java -jar "${ruianExtractorJarFilePath}" "${inputFilePath}" "${outputDirPath}" "${configFilePath}";