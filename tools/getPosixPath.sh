#! /bin/sh
echo "$1" | sed 's|\\|/|g' | sed 's|^\([^:]\):|/\1|'