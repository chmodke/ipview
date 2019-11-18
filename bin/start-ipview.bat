@echo off
%1 mshta vbscript:CreateObject("WScript.Shell").Run("%~s0 ::",0,FALSE)(window.close)&&exit

set IPVIEW_BASE="D:\workspace\java\ipview\target\ipview"
java -server -cp %IPVIEW_BASE%  -Djava.net.preferIPv4Stack=true -Dfile.encoding=UTF-8 org.chmodke.ipview.Starter > %IPVIEW_BASE%\ipview.log