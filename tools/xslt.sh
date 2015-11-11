#! /bin/sh

baseDir="$1";
inputFilePath="$2";
xsltFilePath="$3";

toolsDirPath="${baseDir}/tools";
xsltJarPath="${toolsDirPath}/lib/saxon.jar";

java -jar "${xsltJarPath}" -s:"${inputFilePath}" -xsl:"${xsltFilePath}";