#!/bin/sh

# */5 * * * * /home/kehao/ipview/bin/monitor-ipview.sh

TIME=$(date +'%Y%m%d%H%M')
cmd=`netstat -anp | grep -w 8090 | grep -w LISTEN | wc -l`
if [ "$cmd" -eq "0" ];then
    echo $TIME shutdown.  >> /tmp/ipview.monitor
    source /home/kehao/.bash_profile
    . /home/kehao/ipview/bin/start-ipview.sh
else
    echo $TIME running.  >> /tmp/ipview.monitor
    exit 0
fi
