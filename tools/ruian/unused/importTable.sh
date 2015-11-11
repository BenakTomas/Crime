#! /bin/sh

#baseDir="$1";
inputFilePath="$1";
tableInsertDeclaration="$2";

< "${inputFilePath}" psql -h localhost -p 5432 -U postgres -d gisdb --variable=client_encoding=UTF8 -c "COPY ${tableInsertDeclaration} FROM STDIN WITH DELIMITER '#';";