#! /bin/sh

baseDir="$1";
inputFilePath="$2";
outputDirPath="$3";
configFilePath="$4";

toolsDirPath="${baseDir}/tools";
ruianExtractorJarFilePath="${toolsDirPath}/ruianExtractor.jar";

export namespacesDeclarationFilePath="${baseDir}/templates/namespaces.txt";

function startXmlFile () {
   echo '<?xml version="1.0" encoding="utf-8" ?>
<root '`cat "${namespacesDeclarationFilePath}"`'>' > "$1";
}

function endXmlFile () {
   echo '</root>' >> "$1";
}

export -f startXmlFile;
export -f endXmlFile;

 < "${configFilePath}" cut -d" " -f2 |
#xargs -I file  sh -c 'echo "<root xsi:schemaLocation = \"urn:cz:isvs:ruian:schemas:VymennyFormatTypy:v1 ../ruian/xsd/vymenny_format/VymennyFormatTypy.xsd\" xmlns:gml = \"http://www.opengis.net/gml/3.2\" xmlns:xlink = \"http://www.w3.org/1999/xlink\" xmlns:xsi = \"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ami = \"urn:cz:isvs:ruian:schemas:AdrMistoIntTypy:v1\" xmlns:base = \"urn:cz:isvs:ruian:schemas:BaseTypy:v1\" xmlns:coi = \"urn:cz:isvs:ruian:schemas:CastObceIntTypy:v1\" xmlns:com = \"urn:cz:isvs:ruian:schemas:CommonTypy:v1\" xmlns:kui = \"urn:cz:isvs:ruian:schemas:KatUzIntTypy:v1\" xmlns:kri = \"urn:cz:isvs:ruian:schemas:KrajIntTypy:v1\" xmlns:mci = \"urn:cz:isvs:ruian:schemas:MomcIntTypy:v1\" xmlns:mpi = \"urn:cz:isvs:ruian:schemas:MopIntTypy:v1\" xmlns:obi = \"urn:cz:isvs:ruian:schemas:ObecIntTypy:v1\" xmlns:oki = \"urn:cz:isvs:ruian:schemas:OkresIntTypy:v1\" xmlns:opi = \"urn:cz:isvs:ruian:schemas:OrpIntTypy:v1\" xmlns:pai = \"urn:cz:isvs:ruian:schemas:ParcelaIntTypy:v1\" xmlns:pui = \"urn:cz:isvs:ruian:schemas:PouIntTypy:v1\" xmlns:rsi = \"urn:cz:isvs:ruian:schemas:RegSouIntiTypy:v1\" xmlns:spi = \"urn:cz:isvs:ruian:schemas:SpravObvIntTypy:v1\" xmlns:sti = \"urn:cz:isvs:ruian:schemas:StatIntTypy:v1\" xmlns:soi = \"urn:cz:isvs:ruian:schemas:StavObjIntTypy:v1\" xmlns:uli = \"urn:cz:isvs:ruian:schemas:UliceIntTypy:v1\" xmlns:vci = \"urn:cz:isvs:ruian:schemas:VuscIntTypy:v1\" xmlns:vf = \"urn:cz:isvs:ruian:schemas:VymennyFormatTypy:v1\" xmlns:zji = \"urn:cz:isvs:ruian:schemas:ZsjIntTypy:v1\" xmlns:voi = \"urn:cz:isvs:ruian:schemas:VOIntTypy:v1\">" > '"${outputDirPath}/file";
xargs -I file sh -c 'startXmlFile "'"${outputDirPath}/file"'"';


java -jar "${ruianExtractorJarFilePath}" "${inputFilePath}" "${outputDirPath}" "${configFilePath}";

 < "${configFilePath}" cut -d" " -f2 |
#xargs -I file  sh -c 'echo "<root xsi:schemaLocation = \"urn:cz:isvs:ruian:schemas:VymennyFormatTypy:v1 ../ruian/xsd/vymenny_format/VymennyFormatTypy.xsd\" xmlns:gml = \"http://www.opengis.net/gml/3.2\" xmlns:xlink = \"http://www.w3.org/1999/xlink\" xmlns:xsi = \"http://www.w3.org/2001/XMLSchema-instance\" xmlns:ami = \"urn:cz:isvs:ruian:schemas:AdrMistoIntTypy:v1\" xmlns:base = \"urn:cz:isvs:ruian:schemas:BaseTypy:v1\" xmlns:coi = \"urn:cz:isvs:ruian:schemas:CastObceIntTypy:v1\" xmlns:com = \"urn:cz:isvs:ruian:schemas:CommonTypy:v1\" xmlns:kui = \"urn:cz:isvs:ruian:schemas:KatUzIntTypy:v1\" xmlns:kri = \"urn:cz:isvs:ruian:schemas:KrajIntTypy:v1\" xmlns:mci = \"urn:cz:isvs:ruian:schemas:MomcIntTypy:v1\" xmlns:mpi = \"urn:cz:isvs:ruian:schemas:MopIntTypy:v1\" xmlns:obi = \"urn:cz:isvs:ruian:schemas:ObecIntTypy:v1\" xmlns:oki = \"urn:cz:isvs:ruian:schemas:OkresIntTypy:v1\" xmlns:opi = \"urn:cz:isvs:ruian:schemas:OrpIntTypy:v1\" xmlns:pai = \"urn:cz:isvs:ruian:schemas:ParcelaIntTypy:v1\" xmlns:pui = \"urn:cz:isvs:ruian:schemas:PouIntTypy:v1\" xmlns:rsi = \"urn:cz:isvs:ruian:schemas:RegSouIntiTypy:v1\" xmlns:spi = \"urn:cz:isvs:ruian:schemas:SpravObvIntTypy:v1\" xmlns:sti = \"urn:cz:isvs:ruian:schemas:StatIntTypy:v1\" xmlns:soi = \"urn:cz:isvs:ruian:schemas:StavObjIntTypy:v1\" xmlns:uli = \"urn:cz:isvs:ruian:schemas:UliceIntTypy:v1\" xmlns:vci = \"urn:cz:isvs:ruian:schemas:VuscIntTypy:v1\" xmlns:vf = \"urn:cz:isvs:ruian:schemas:VymennyFormatTypy:v1\" xmlns:zji = \"urn:cz:isvs:ruian:schemas:ZsjIntTypy:v1\" xmlns:voi = \"urn:cz:isvs:ruian:schemas:VOIntTypy:v1\">" > '"${outputDirPath}/file";
xargs -I file sh -c 'endXmlFile "'"${outputDirPath}/file"'"';