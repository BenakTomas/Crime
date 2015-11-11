#! /bin/sh
year=$1;
limit=$2;
ontologyBaseDir="C:\crimestatistics\ontology";
d2rqExportedDataBaseDir="C:\crimestatistics\d2rq";
baseDir="C:\crimestatistics";
namedGraphUri="http://linked.opendata.cz/resource/domain/crimestatistics/dataset";

"${baseDir}/tools/extractAndImportData.sh" $year $limit "${ontologyBaseDir}" "${d2rqExportedDataBaseDir}" "${baseDir}" "${namedGraphUri}"
