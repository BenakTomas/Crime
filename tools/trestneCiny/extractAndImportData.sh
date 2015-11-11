#! /bin/sh
year=$1;
limit=$2;
ontologyBaseDir="$3";
d2rqExportedDataBaseDir="$4";
baseDir="$5";
importToGraphURI="$6";

virtuosoServiceName="virtuoso";
mysqlServiceName="mysql56";
d2rqHomePath="C:/d2r/d2rq-0.8.1";
dataToImportStoreDir="${baseDir}/data";
dataToImportStoreDirForVirtuoso=`echo "${dataToImportStoreDir}" | tr '\' '/'`;

dumpRdfScriptFilePath="${d2rqExportedDataBaseDir}/tools/dumprdf.sh";

# stop Virtuoso
#net stop "OpenLink Virtuoso Server [${virtuosoServiceName}]";

# start mysql
net start "${mysqlServiceName}";

rm "${dataToImportStoreDir}/"*.n3 2> /dev/null;
# export data using D2RQ
echo "Exporting data with D2RQ..."
"${dumpRdfScriptFilePath}" $year $limit "${d2rqExportedDataBaseDir}" "${dataToImportStoreDir}" "${d2rqHomePath}" ;
echo "done."

# stop mysql
net stop "${mysqlServiceName}";

# prepare data for import into Virtuoso
# 1) delete all present TTL and N3 import files
rm "${dataToImportStoreDir}/"*.ttl 2> /dev/null;

# copy data from crimestatistics ontology project directory
find "${ontologyBaseDir}/ontology" -name '*.ttl' -print0 | xargs -0 -I file cp file "${dataToImportStoreDir}";
find "${ontologyBaseDir}/resource/dataset/definitions" -name '*.ttl' -print0 | xargs -0 -I file cp file "${dataToImportStoreDir}";

# 2) load data into virtuoso via isql
# start Virtuoso
net start "OpenLink Virtuoso Server [${virtuosoServiceName}]";
isql 1111 dba dba exec="SPARQL CLEAR GRAPH <${importToGraphURI}>; delete from db.dba.load_list ; ld_dir('${dataToImportStoreDirForVirtuoso}', '*.ttl', '${importToGraphURI}') ; ld_dir('${dataToImportStoreDirForVirtuoso}', '*.n3', '${importToGraphURI}'); rdf_loader_run() ;";

touch "${dataToImportStoreDir}/objasnene.n3";
touch "${dataToImportStoreDir}/neobjasnene.n3";