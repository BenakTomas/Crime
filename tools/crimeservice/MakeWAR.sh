#! /bin/sh
webAppName="$1";
webAppDir="$2";
webAppDeployDir="$3";
webAppClassPath="$4";

webAppWarFileDeployPath="${webAppDeployDir}/${webAppName}.war";
webAppDeployFolder="${webAppDeployDir}/${webAppName}";

rm -r "${webAppDeployFolder}" 2> /dev/null;
rm "${webAppWarFileDeployPath}" 2> /dev/null;

javac -g -cp "${webAppDir}\WEB-INF\lib\*;${webAppClassPath}" -d "${webAppDir}\WEB-INF\classes" `find "${webAppDir}\src" -name '*.java'`

jar cvf "${webAppWarFileDeployPath}" -C "${webAppDir}" "./WEB-INF" "./META-INF";