#!/bin/bash
####################################################
# The script checks if servers are down
# Sends an e-mail if they are.
# Install it as a cron-job.
# 
# Author: Stein Inge Morisbak (stein.inge.morisbak@BEKK.no)
#

JAVA_HOME=/usr/local/java
APP_HOME=/server/bekkopen/webapp

hostname=`hostname`
PID=`ps -ea -o "pid ppid args" | grep -v grep | grep "${JAVA_HOME}/bin/java" \
  | grep "classpath :${APP_HOME}/etc" | sed -e 's/^  *//' -e 's/ .*//' | head -1`
if [ ! "$PID" ]; then
  echo "Sever $hostname is down." | mail -s "Server $hostname is down (eom)" "stein.inge.morisbak@BEKK.no"
fi
