SPARQL CLEAR GRAPH <http://localhost:8890/crimestatistics/trestneCiny>; delete from db.dba.load_list ; ld_dir('/data/benak', '*.ttl', 'http://localhost:8890/crimestatistics/trestneCiny') ; ld_dir('/data/benak', '*.n3', 'http://localhost:8890/crimestatistics/trestneCiny'); rdf_loader_run() ;