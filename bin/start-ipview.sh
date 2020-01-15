#!/bin/sh
#cd $HOME/ipview/deploy
IPVIEW_BASE=$HOME/ipview/deploy
HOST_IP="`/sbin/ifconfig -a | grep inet | grep -v 127.0.0.1 | grep -v inet6 | awk '{print $2}' | tr -d 'addr:'`"

JMX_PORT=18091
HTTP_PORT=8090
JAVA_OPTS="$JAVA_OPTS -Djava.rmi.server.hostname=$HOST_IP"
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote"
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.port=$JMX_PORT"
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.local.only=false"
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.authenticate=false"
JAVA_OPTS="$JAVA_OPTS -Dcom.sun.management.jmxremote.ssl=false"

if [ -n "`jps -l | grep 'org.chmodke.ipview.Starter' | awk {'print $1'}`" ] ;then
    echo "process org.chmodke.ipview.Starter already running...";
    exit 1;
fi

while [ 0 -ne $(netstat -anp | grep -w $HTTP_PORT | wc -l) ]; do
    echo '未释放完成，等待中...'
    sleep 5;
done

java $JAVA_OPTS -server -cp $IPVIEW_BASE -Djava.net.preferIPv4Stack=true -Dfile.encoding=UTF-8 org.chmodke.ipview.Starter 2>&1 |\
cronolog $HOME/ipview/log/ipview-%m%d.log > /dev/null &
#cd -