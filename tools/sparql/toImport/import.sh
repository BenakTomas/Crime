 #!/bin/sh
baseDir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )";
fromDir="$1";
ttlDir="${baseDir}/ttl";
n3Dir="${baseDir}/n3";

rm "${ttlDir}/"*.ttl 2> /dev/null;
rm "${n3Dir}/"*.n3 2> /dev/null;

find "$fromDir/ontology" "$fromDir/resource" -name '*.ttl' -print0 | xargs -0 -I file cp file "${ttlDir}"
find "$fromDir/resource" -name '*.n3' -print0 | xargs -0 -I file cp file "${n3Dir}"

isql 1111 dba dba exec="SPARQL CLEAR GRAPH <http://localhost:8890/crimestatistics/trestneCiny>; delete from db.dba.load_list ; ld_dir('../data/toImport/ttl', '*.ttl', 'NULL') ; ld_dir('../data/toImport/n3', '*.n3', 'NULL'); rdf_loader_run() ;"