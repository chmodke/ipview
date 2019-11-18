#!/bin/sh
#cd $HOME/ipview/deploy
IPVIEW_BASE=$HOME/ipview/deploy
java -server -cp $IPVIEW_BASE -Djava.net.preferIPv4Stack=true -Dfile.encoding=UTF-8 org.chmodke.ipview.Starter 2>&1 |\
cronolog $HOME/ipview/log/ipview-%m%d.log > /dev/null &
#cd -
