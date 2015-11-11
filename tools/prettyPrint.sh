#! /bin/sh

baseDir="$CRIME_HOME";
toolsDir="${baseDir}/tools";
prettyPrinterJarFilePath="${toolsDir}/prettyprinter.jar";

inputXmlFilePath="$1";
outputXmlFilePath=`dirname "${inputXmlFilePath}"`"/"`basename "${inputXmlFilePath}" ".xml"`"_pretty.xml";

java -jar "${prettyPrinterJarFilePath}" "${inputXmlFilePath}" > "${outputXmlFilePath}";