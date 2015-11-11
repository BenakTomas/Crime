#! /bin/sh
baseDir="../data/toImport";

if [ $# -eq 1 ]
then
	year=$1;
	importOntologyDir="${baseDir}/${year}/ontology";
	importDataDir="${baseDir}/${year}/data";
else
	importOntologyDir="${baseDir}/ontology";
	importDataDir="${baseDir}/data";
fi

isql 1111 dba dba exec="SPARQL CLEAR GRAPH <http://localhost:8890/crimestatistics/trestneCiny>; delete from db.dba.load_list ; ld_dir('${importOntologyDir}', '*.ttl', 'NULL') ; ld_dir('${importDataDir}', '*.ttl', 'NULL');  ld_dir('${importDataDir}', '*.n3', 'NULL'); rdf_loader_run() ;";