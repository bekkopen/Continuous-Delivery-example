#!/bin/bash

###### FUNKSJONER #########

upload_file() {
  local server=$1
  local file=$2
  local target=$3
  if [ -f $file ]; then
    echo "Uploading $file to $server:$target"
    scp $file $server:$target
    if [ "$?" -ne "0" ]; then
      echo "The command scp $file $user@$server:$target failed! Quitting ..."
      exit 800
    fi
  else
    echo "File not found: $file"
    exit 801
  fi
}

function contains() {
  local n=$#
  local value=${!n}
  for ((i=1;i < $#;i++)) {
    if [ "${!i}" == "${value}" ]; then
      echo "y"
      return 0
    fi
  }
  echo "n"
  return 1
}

##########################

servers=( "node1" "node2" "node3")

if [ $# -lt 1 ]; then
   echo 1>&2 "Usage: $0 [<server> ...]"
   echo 1>&2 "Valid servers: ${servers[@]}"
   exit 802
fi

arguments=( $@ )

for i in $*
do
  if [ $(contains "${servers[@]}" $i) == "n" ]; then
    echo "Invalid server: $i"
    exit 803
  fi
done

version="`grep artifactId.*parent ../pom.xml -A1 | grep version | sed -E 's/.*<version>(.*)<\/version>/\1/'`"
read -p "Version? [$version] " input_version
if [ $input_version ]; then
  version=$input_version
fi
yn=y
while true; do
    read -p "Do you wish to upload the app from local machine? [$yn] " input_yn
    if [ $input_yn ]; then
      yn=$input_yn
    fi
    case $yn in
      [Yy]* ) deploy_from_local_files="true"
      break;;
      [Nn]* ) deploy_from_local_files="false"
      break;;
      * ) echo "You must answer yes or no.";;
    esac
done
if [ "true" == $deploy_from_local_files ]; then
  yn=y
  while true; do
    read -p "Have you remembered to run mvn clean install? [$yn] " input_yn
    if [ $input_yn ]; then
      yn=$input_yn
    fi
    case $yn in
      [Yy]* ) break;;
      [Nn]* ) exit;;
      * ) echo "You must answer yes or no.";;
    esac
  done
fi

candidate_artifacts=( "webapp" )
declare -a artifacts
for artifact in ${candidate_artifacts[@]}
do
while true; do
      yn=y
      read -p "Do you wish to deploy $artifact? [$yn] " input_yn
      if [ $input_yn ]; then
        yn=$input_yn
      fi
      case $yn in
        [Yy]* ) artifacts[$[${#artifacts[@]}+1]]=$artifact
        break;;
        [Nn]* )
        break;;
        * ) echo "You must answer yes or no.";;
      esac
  done
done

if [ ${#artifacts[@]} -eq 0 ]; then
  echo "You must choose at least one artifact!"
  exit 0;
fi

echo The following artifacts will be deployed: ${artifacts[@]}

server_suffix=".morisbak.net"
home="./"
script_dir="."
config_dir="../config"
target_config_file="deploy.config"

targets=${arguments[@]}
declare -a deploy_cmds

for target in ${targets[@]}
do
  server=$target
  user="bekkopen"
  config_file="$config_dir/$target/deploy.config"
  startup_script="$script_dir/$target/startup.sh"
  deploy_script="$script_dir/$target/deploy.sh"
  server_host="$user@$server$server_suffix"
  if [ "true" == $deploy_from_local_files ]; then
    upload_file $server_host $startup_script $home
    upload_file $server_host $deploy_script $home
    upload_file $server_host $config_file "$home/$target_config_file"
  fi
  for artifact in ${artifacts[@]}
  do
    if [ "true" == $deploy_from_local_files ]; then
      upload_file $server_host "../$artifact/target/$artifact-$version.zip" $home
    fi
    cmd="ssh -tt $server_host \"cd $home ; nohup ./deploy.sh $artifact $version > /dev/null 2>&1 </dev/null\""
    echo "Running: $cmd"
    eval $cmd
    response=$?
    if [ $response -ne 0 ]; then
      echo "$cmd failed with exit code ${response}! Quitting ..."
      exit 805
    fi
  done
done
