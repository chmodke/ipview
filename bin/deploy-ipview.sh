#!/bin/bash
IPVIEW_BASE=$HOME/ipview
IPVIEW_GIT=kehao@192.168.100.100:/home/kehao/data/git/ipview.git

if [ ! -d $IPVIEW_BASE/src ] ; then
    git clone $IPVIEW_GIT $IPVIEW_BASE/src
fi
cd $IPVIEW_BASE/src
git checkout develop
git pull --rebase origin develop

mvn -f $IPVIEW_BASE/src/pom.xml clean
mvn -f $IPVIEW_BASE/src/pom.xml assembly:assembly -Dmaven.test.skip=true

cd $IPVIEW_BASE/deploy
[ -f $IPVIEW_BASE/src/target/ipview.jar ] && rm -rf ./*
cp $IPVIEW_BASE/src/target/ipview.jar ./
 
jar -xf ipview.jar
rm -f ipview.jar
cp $IPVIEW_BASE/app.properties ./app.properties
cd -
