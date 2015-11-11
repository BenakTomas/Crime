#! /bin/sh

year=$1;
exportDataDir="$2";
crimeMonitorBaseDir="C:\crimestatistics";
ontologyBaseDir="C:\crimestatistics\ontology";
d2rqExportedDataBaseDir="C:\crimestatistics\d2rq";
namedGraphUri="http://linked.opendata.cz/resource/domain/crimestatistics/dataset";

"${crimeMonitorBaseDir}/tools/extractData.sh" $year "${exportDataDir}" "${crimeMonitorBaseDir}" "${ontologyBaseDir}" "${d2rqExportedDataBaseDir}" "${namedGraphUri}"