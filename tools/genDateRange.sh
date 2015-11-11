#! /bin/sh

date_From="$1";
date_To="$2";

date_var="${date_From}";

while [ `date -d "${date_var}" +%s` -le `date -d "${date_To}" +%s` ]
do
  echo "${date_var}";
  date_var=`date -d "${date_var} 1 day" +%Y%m%d`
done;

