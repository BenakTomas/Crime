#!/bin/sh
baseDir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )";
year=$1;

# split files
split -l 80 "${baseDir}/${year}/okresy/okresy_actual.csv" "${baseDir}/${year}/okresy/okresy_actual_";

# run extraction
"${baseDir}/utvarExtractor.exe" "${baseDir}/${year}/okresy/okresy_actual_aa" "${baseDir}/${year}/utvary/source/xls" > "${baseDir}/${year}/utvary/utvary_actual.csv" 2> "${baseDir}/${year}/utvary/utvary_error_log.txt"
"${baseDir}/utvarExtractor.exe" "${baseDir}/${year}/okresy/okresy_actual_ab" "${baseDir}/${year}/utvary/source/xls" >> "${baseDir}/${year}/utvary/utvary_actual.csv" 2>> "${baseDir}/${year}/utvary/utvary_error_log.txt"