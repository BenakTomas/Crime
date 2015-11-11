#! /bin/sh
year=$1;
exportedDataStoreDir="$2";
crimeMonitorBaseDir="$3";
ontologyBaseDir="$4";
d2rqExtractedDataBaseDir="$5";
importToGraphURI="$6";

mysqlServiceName="mysql56";
d2rqHomePath="C:/d2r/d2rq-0.8.1";

dumpRdfScriptFilePath="${d2rqExtractedDataBaseDir}/tools/dumprdf_unlimited.sh";

# start mysql
net start "${mysqlServiceName}";

# delete all present TTL and N3 import files
rm "${exportedDataStoreDir}/"*.n3 2> /dev/null;
rm "${exportedDataStoreDir}/"*.ttl 2> /dev/null;

# export data using D2RQ
echo "Exporting data with D2RQ..."
"${dumpRdfScriptFilePath}" $year "${exportedDataStoreDir}" "${d2rqExtractedDataBaseDir}"  "${d2rqHomePath}" ;
echo "done."

# stop mysql
net stop "${mysqlServiceName}";

# copy data from crimestatistics ontology project directory
find "${ontologyBaseDir}/ontology" -name '*.ttl' -print0 | xargs -0 -I file cp file "${exportedDataStoreDir}";
find "${ontologyBaseDir}/resource/dataset/definitions" -name '*.ttl' -print0 | xargs -0 -I file cp file "${exportedDataStoreDir}";