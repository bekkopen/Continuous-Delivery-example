#!/bin/sh
#
# Author: Stein Inge Morisbak (e-mail: stein.inge.morisbak@BEKK.no)
#
# Start up script
# The script is adapted for start/stop at boot/shutdown of the OS.
# Installs as /etc/init.d/<app-name>
# Modify the variables below for your set-up.
#
# chkconfig: 2345 20 80
# description: Starts and stops <application>
#

APP_NAME=webapp
#JAVA_HOME=$(/usr/libexec/java_home)
HOME_DIR=/server/bekkopen
USER=bekkopen

#
# DO NOT CHANGE BELOW THIS LINE
#
APP_HOME=${HOME_DIR}/${APP_NAME}
APPDIR=${APP_HOME}/bin
STARTSCRIPT=${APPDIR}/${APP_NAME}
LOGFILE=${HOME_DIR}/logs/${APP_NAME}.log
LC_ALL=no_NO.utf8

umask 002

export JAVA_HOME APPDIR LC_ALL

PID=`ps -ea -o "pid ppid args" | grep -v grep | grep "${JAVA_HOME}/bin/java" \
    | grep "classpath :${APP_HOME}/etc" | sed -e 's/^  *//' -e 's/ .*//' | head -1`

############## FUNCTIONS ################################################

_start()
{
   cd ${APPDIR}
   if [ "`whoami`" = "$USER" ]
   then
     sh -x ${STARTSCRIPT} 1>>${LOGFILE} 2>&1 & RETVAL=$?
   else
     su "${USER}" -c "sh -x ${STARTSCRIPT} 1>>${LOGFILE} 2>&1 & RETVAL=$?"
   fi
   if [ "$?" = "0" ]
   then
     echo "Ran startup command for ${APP_NAME}. Check the log: ${LOGFILE})"
     RETVAL=0
   else
     echo "Could not start ${APP_NAME} :-("
     RETVAL=1
   fi
}

_stopp()
{
  echo "Stopping ${APP_NAME}..." >>${LOGFILE}
  kill ${PID} && sleep 5 && $0 status && \
  kill ${PID} && sleep 5 && $0 status && \
  kill -INT ${PID} && sleep 5 && $0 status && \
  kill -INT ${PID} && sleep 5 && $0 status && \
  kill -KILL ${PID} && sleep 5 && $0 status && \
  kill -KILL ${PID} && sleep 5 && $0 status
  RETVAL=$?
  $0 status 1>>${LOGFILE} 2>&1
}

################################################################################

case $1 in
  start)
    if [ "${PID}" != "" ]
    then
      echo "${APP_NAME} is already started (PID=${PID})"
      RETVAL=1
    else
      _start
    fi
    ;;
  stop)
    if [ "${PID}" != "" ]
    then
      _stopp
    else
      echo "${APP_NAME} is not started"
      RETVAL=1
    fi
    ;;
  restart)
    if [ "${PID}" != "" ]
    then
      _stopp
      _start
    else
      echo "${APP_NAME} is not started"
      RETVAL=1
    fi
    ;;

  status)
    kill -0 ${PID} >/dev/null 2>&1
    if [ "$?" = "0" ]
    then
      echo "${APP_NAME} (pid ${PID}) is running"
      RETVAL=0
    else
      echo "${APP_NAME} has stopped"
      RETVAL=1
    fi
    ;;

  *)
    echo "Usage: $0 { start | stop | restart | status }"
    exit 1
    ;;

esac
exit ${RETVAL}
