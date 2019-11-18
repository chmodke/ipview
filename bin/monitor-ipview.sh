#!/bin/sh

# */5 * * * * /home/kehao/ipview/bin/monitor-ipview.sh

cmd=`netstat -anp | grep -w 8091 | grep -w LISTEN | wc -l`
if [ "$cmd" -eq "0" ];then
    sh /home/kehao/ipview/bin/start-ipview.sh
    exit
else
    exit 0
fi
