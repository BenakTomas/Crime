#! /bin/sh

year=$1;
baseDir="$2";

templateFilePath="${baseDir}/templates/utvar.template";
utvarYearPath="${baseDir}/${year}";
zakladniUtvarySourceFilePath="${utvarYearPath}/source/zakladniUtvary.txt";
zakladniUtvaryOutputFilePath="${utvarYearPath}/output/zakladniUtvary.ttl";

transformScript="${baseDir}/tools/createUtvary.awk";


< "${templateFilePath}" cat > "${zakladniUtvaryOutputFilePath}";
< "${zakladniUtvarySourceFilePath}" "${transformScript}" >> "${zakladniUtvaryOutputFilePath}";  