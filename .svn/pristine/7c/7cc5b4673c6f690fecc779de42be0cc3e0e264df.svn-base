@echo off
rem outer.bat
set service="outer-start-0.0.1-SNAPSHOT.jar"
set servicename="outer service"
set port=1099
set JAVA_OPTS= -Xms128m -Xmx512m -XX:PermSize=128m
cd ..
if not exist log md log
if exist temp.txt del temp.txt
:start
netstat -aon | findstr "%port%"
for /f "tokens=5 delims= " %%i in ('netstat -aon ^| findstr "%port%"') do @echo %%i >temp.txt
if exist temp.txt goto check
echo %servicename% start,Please wait...
start /b java %JAVA_OPTS% -jar %service% 
rem >> .\log\outer-%date:~0,4%%date:~5,2%%date:~8,2%.log 2>&1
rem type .\log\outer-%date:~0,4%%date:~5,2%%date:~8,2%.log
rem echo "%servicename% has been started."
goto end
:check
cls
echo "%servicename% has been started."
pause
goto end
:end
exit