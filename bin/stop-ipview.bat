@echo off
rem 设置java启动的主类
set MainClass=org.chmodke.ipview.Starter
echo MainClass : %MainClass%

for /f "usebackq tokens=1-2" %%a in (`jps -l ^| findstr "%MainClass%"`) do (
	if [%%b] EQU [%MainClass%] (
		set pid=%%a
	)
)

for /f "usebackq tokens=1-5" %%a in (`tasklist ^| findstr %pid%`) do (
	set image_name=%%a
)

echo now will kill process : pid %pid%, image_name %image_name%
pause
rem 根据进程ID，kill进程
taskkill /f /pid %pid%
pause