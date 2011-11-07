#!/bin/sh

year=$(date +"%Y")
month=$(date +"%m")
day=$(date +"%d")

echo Tailing logs for ${year}_${month}_${day}

multitail \
  -l "ssh bekkopen@node2.morisbak.net tail -1000f /server/bekkopen/logs/stderrout.${year}_${month}_${day}.log" \
  -l "ssh bekkopen@node3.morisbak.net tail -1000f /server/bekkopen/logs/stderrout.${year}_${month}_${day}.log"

