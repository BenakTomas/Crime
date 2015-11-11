#! /bin/sh

baseDir="$( dirname "${BASH_SOURCE[0]}" )";

"${baseDir}/makewar.sh" "crimeservice" "C:\crimestatistics\crimeservice\deploy" "C:\apache\tomcat\apache-tomcat-7.0.52\webapps" "c:/apache/tomcat/apache-tomcat-7.0.52/lib/servlet-api.jar"
net start "OpenLink Virtuoso Server [virtuoso]"
net start "postgresql-9.3 - PostgreSQL Server 9.3"

