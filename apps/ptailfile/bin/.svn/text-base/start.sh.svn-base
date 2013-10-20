#!/bin/bash

export PYTHONPATH=/home/admin/timetunnel/pclient/src/:$PYTHONPATH

base_dir=`dirname $0`
PID_FILE=${base_dir}/.ptailfile.pid;

script=${base_dir}/../src/tailfile.py

startup_log=/tmp/ptailfile.start

echo ${script} > ${startup_log}

python ${script} 1>/dev/null 2>>${startup_log} &

sleep 3

pid=$!

pids=`ps aux | grep python | grep tailfile | awk '{print $2;}'`;
if [ -z $pids ];
then
	echo "start failed, check /tmp/ptailfile.start";
        exit -1;
fi

for id in $pids
do
	if [ $id -eq $pid ];
        then
		echo "process is running as $pid.";
		echo ${pid}>${PID_FILE};
	   	exit 0;
        fi
done
echo "start failed, check /tmp/ptailfile.start";
exit -1;


