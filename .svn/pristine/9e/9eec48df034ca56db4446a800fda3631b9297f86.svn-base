@echo off
rem outer.bat
set service="outer-start-0.0.1-SNAPSHOT.jar"
set servicename="outer service"
set port=1099
cd ..
if exist temp.txt del temp.txt
:stop
netstat -aon | findstr "%port%"
for /f "tokens=5 delims= " %%i in ('netstat -aon ^| findstr "%port%"') do @echo %%i >temp.txt
if not exist temp.txt goto error
if exist temp.txt goto killids
:killids
set /p count=<temp.txt
cls 
echo %servicename% stop,Please wait...
tskill %count%
pause
goto end
:error
cls
echo "%servicename%" has not start,Please check.
pause
:end