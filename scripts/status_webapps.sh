#!/bin/sh

server_suffix=".morisbak.net"
servers=( "node2" "node3")

for server in ${servers[@]}
do
  echo "$server:"
  server_host=$server$server_suffix
  cmd="ssh bekkopen@$server_host \"./startup.sh status\""
  eval $cmd
  echo ""
done
