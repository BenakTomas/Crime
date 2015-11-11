#! /bin/sh

sourceDirPath="$1";
destDirPath="$2";

while read file
do
	extractedXmlFileName=`basename "${file}" .gz`;
	extractedXmlFilePath="${destDirPath}/${extractedXmlFileName}";
	
	gzip -cd "${file}" |
	tail -n +2 > "${extractedXmlFilePath}";
done <<EOT
$(ls -1 "${sourceDirPath}/"*.gz)
EOT