#!/bin/bash

PROCESS='org.chmodke.ipview.Starter'

while [ -n "`jps -l | grep $PROCESS | awk {'print $1'}`" ]; do
    jps -l | grep $PROCESS | awk {'print $1'} | xargs kill;
    sleep 5;
    if [ -z "`jps -l | grep $PROCESS | awk {'print $1'}`" ] ;then
        echo "process org.chmodke.ipview.Starter is terminated";
        exit 0;
    else 
        echo "process org.chmodke.ipview.Starter is not terminated";
    fi
done