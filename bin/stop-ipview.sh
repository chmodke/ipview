#!/bin/sh

jps -l | grep 'org.chmodke.ipview.Starter' | awk {'print $1'}  |xargs kill
