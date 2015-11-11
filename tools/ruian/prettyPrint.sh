#! /bin/sh

baseDir="$1";
toolsDir="${baseDir}/tools";
prettyPrinterJarFilePath="${toolsDir}/prettyprinter.jar";

inputXmlFilePath="$2";
outputXmlFilePath=`dirname "${inputXmlFilePath}"`"/"`basename "${inputXmlFilePath}" ".xml"`"_pretty.xml";

java -jar "${prettyPrinterJarFilePath}" "${inputXmlFilePath}" > "${outputXmlFilePath}";