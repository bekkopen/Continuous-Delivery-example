#!/bin/sh
#########################################################################
# Deploy script.
# Depends on ./deploy.config
#
# Usage: deploy.sh <artifact> <version>
# Examples version: 1.0.0, 1.0.0-SNAPSHOT, 1.0.0.backup, 1.0.0-SNAPSHOT.backup
#
# Author: Stein Inge Morisbak (e-mail: stein.inge.morisbak@BEKK.no)
#########################################################################

if [ -f ./deploy.config ]; then
  . ./deploy.config
else
  echo 1>&2 "Could not find ./deploy.config. Can not continue :-("
  exit 127
fi

ARTIFACT=$1
VERSION=$2

############## FUNCTIONS ################################################

_run()
{
   echo 1>&2 Running $1 ...
   $1
   RESPONSE=$?

   if [ $RESPONSE -ne 0 ]; then
     echo 1>&2 "$1: command failed :-("
     _rollBack
     exit 127
   fi
   return $RESPONSE
}

_delete()
{
   echo 1>&2 Deleting $1 ...
   if [ -d $1 ]; then
      rm -Rf $1
   elif [ -f $1 ]; then
      rm -f $1
   else
      echo $1: Directory or file does not exist. Nothing to delete. 1>&2
   fi
   return 0
}

_rollBack()
{
   echo 1>&2 "Rolling back."
   if [ -f ${PREVIOUS_VERSION}.backup.zip ]; then
      _stopp
      _delete ${ARTIFACT}
      if [ -d $DIRECTORY_NAME ]; then
         _delete $DIRECTORY_NAME
      fi
      unzip -qq -o ${PREVIOUS_VERSION}.backup.zip
      ln -s ${PREVIOUS_VERSION} ${ARTIFACT}
      _start
      if [ "$?" -eq "0" ]; then
         echo 1>&2 "Rollback to ${PREVIOUS_VERSION} successful :-)"
      else
         echo 1>&2 "${ARTIFACT} did not start. Rollback to ${PREVIOUS_VERSION} failed :-("
      fi
   else
      echo 1>&2 "Could not find ${PREVIOUS_VERSION}.backup.zip. Unable to roll back :-("
      _start
      exit 127
   fi
   return 0
}

_isRunning()
{
   STATUS=`${START_SCRIPT} status`
   echo 1>&2 "The status of ${ARTIFACT} is: ${STATUS}"
   case ${STATUS} in
      "${ARTIFACT} has stopped")
      return 0 ;;
   esac
   return 1
}

_start()
{
   echo 1>&2 Starting ${ARTIFACT} ...
   ${START_SCRIPT} start
   RETVAL=$?
   if [ $RETVAL -ne 0 ]; then
      return 127
   fi
   return 0
}

_stopp()
{
   echo 1>&2 Stopping ${ARTIFACT} ...
   ${START_SCRIPT} stop
   RETVAL=$?
   if [ $RETVAL -ne 0 ]; then
      return 127
   fi
   return 0
}

################################################################################

if [ $# -ne 2 ]; then
   echo 1>&2 "Usage: $0 \<artifact\> \<version\> (examples version: 1.0.0, 1.0.0-SNAPSHOT, 1.0.0.backup, 1.0.0-SNAPSHOT.backup)"
   exit 127
fi

# Check if START_SCRIPT is configured
if [ "$START_SCRIPT" = "" ]; then
   echo 1>&2 "Start script is not defined: You must configure START_SCRIPT in ./deploy.config"
   exit 127
fi

# Check if START_SCRIPT exists
if [ ! -f "$START_SCRIPT" ]; then
   echo 1>&2 "Start script does not exist: $START_SCRIPT. Set the correct value for START_SCRIPT in ./deploy.config"
   exit 127
fi

case $VERSION in
      [0-9]\.[0-9]*)
      echo 1>&2 "Deploying ${ARTIFACT}-${VERSION}.zip ..." ;;
      *)
      echo 1>&2 "Illegal version: ${VERSION}. (Examples of legal versions: 1.0.0, 1.0.0-SNAPSHOT, 1.0.0.backup, 1.0.0-SNAPSHOT.backup)"
      exit 127 ;;
   esac

VERSION_NUMBER=`echo $VERSION | awk -F- '{print $1}' | awk -F.backup '{print $1}'`
NEXUS_REPO=releases
PREVIOUS_VERSION=`readlink ${ARTIFACT} | awk -F/ '{print $1}'`
SNAPSHOT_TRUE_FALSE=false
ROLLBACK_TRUE_FALSE=false
DIRECTORY_NAME=${ARTIFACT}-${VERSION_NUMBER}

case $VERSION in
   *SNAPSHOT*)
   SNAPSHOT_TRUE_FALSE=true ;;
   *[0-2]0[0-9][0-9][0-1][0-9][0-3][0-9]*)
   SNAPSHOT_TRUE_FALSE=true ;;
esac

case $VERSION in
   *backup*)
   ROLLBACK_TRUE_FALSE=true ;;
esac

if [ "$SNAPSHOT_TRUE_FALSE" = "true" ]; then
   NEXUS_REPO=snapshots
   DIRECTORY_NAME=${ARTIFACT}-${VERSION_NUMBER}-SNAPSHOT
fi

URL="${NEXUS_URL}/service/local/artifact/maven/content?r=${NEXUS_REPO}&g=${GROUP_ID}&a=${ARTIFACT}&v=${VERSION}&e=zip"

echo 1>&2 VERSION=$VERSION
echo 1>&2 VERSION_NUMBER=$VERSION_NUMBER
echo 1>&2 SNAPSHOT_TRUE_FALSE=$SNAPSHOT_TRUE_FALSE
echo 1>&2 ROLLBACK_TRUE_FALSE=$ROLLBACK_TRUE_FALSE
echo 1>&2 PREVIOUS_VERSION=$PREVIOUS_VERSION
echo 1>&2 DIRECTORY_NAME=$DIRECTORY_NAME

# Download or deploy local file
if [ -f ${ARTIFACT}-${VERSION}.zip ]; then
   echo 1>&2 Found ${ARTIFACT}-${VERSION}.zip locally. Ready for deploy ...
elif [ "$ROLLBACK_TRUE_FALSE" = "false" ]; then
   echo 1>&2 Did not find ${ARTIFACT}-${VERSION}.zip locally. Downloading from Nexus ...
   _run "wget ${URL} -O ${ARTIFACT}-${VERSION}.zip"
   if [ "$?" -ne "0" ]; then
      _delete ${ARTIFACT}-${VERSION}.zip
   fi
elif [ "$ROLLBACK_TRUE_FALSE" = "true" ]; then
   echo 1>&2 Did not find ${ARTIFACT}-${VERSION}.zip localy. Nothing to do ...
   exit 127
fi

_stopp

# Back up previous version if this is not a rollback
if [ "$PREVIOUS_VERSION" = "" ]; then
   echo 1>&2 Found no previous versions. Nothing to replace ...
else
   _delete ${ARTIFACT} # softlink
   if [ -d $PREVIOUS_VERSION ] && [ "$ROLLBACK_TRUE_FALSE" = "false" ]; then
      _delete ${PREVIOUS_VERSION}.backup.zip
      _run "zip -r -q ${PREVIOUS_VERSION}.backup.zip ${PREVIOUS_VERSION}"
   fi
   _delete ${PREVIOUS_VERSION}
fi

_run "unzip -qq ${ARTIFACT}-${VERSION}.zip"

_delete ${ARTIFACT}-${VERSION}.zip

_run "ln -s $DIRECTORY_NAME ${ARTIFACT}"

_start

if [ "$?" -eq "0" ]; then
   echo 1>&2 "Deployed ${ARTIFACT}-${VERSION} to $DIRECTORY_NAME successfully :-)"
else
   echo 1>&2 "${ARTIFACT} did not start. Deploy failed :-("
fi
