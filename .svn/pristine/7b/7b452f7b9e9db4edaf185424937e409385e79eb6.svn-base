@echo off
rem outer.bat
set service="outer-start-0.0.1-SNAPSHOT.jar"
set servicename="outer service"
set port=1099
set JAVA_OPTS= -Xms128m -Xmx512m -XX:PermSize=128m
cd ..
if not exist log md log
if exist temp.txt del temp.txt
if "%1" == "start" goto start
if "%1" == "stop"  goto stop
if "%1" == "status"  goto status
if "%1" == "restart"  goto restart
echo **********************************************************************
echo Welcome to use outer service
echo **********************************************************************
echo 1:start outer service.
echo 2:stop outer service.
echo 3:check outer service.
echo 4:restart outer service.
echo 5:exit program
echo Please choose:
choice /C "12345"
rem echo %errorlevel%
if errorlevel 5 goto end
if errorlevel 4 goto restart
if errorlevel 3 goto status
if errorlevel 2 goto stop
if errorlevel 1 goto start
:restart
netstat -aon | findstr "%port%"
for /f "tokens=5 delims= " %%i in ('netstat -aon ^| findstr "%port%"') do @echo %%i >temp.txt
if not exist temp.txt goto start
if exist temp.txt goto killstart
:killstart
set /p count=<temp.txt
cls
echo %servicename% stop,Please wait...
tskill %count%
if exist temp.txt del temp.txt
goto start
:start
netstat -aon | findstr "%port%"
for /f "tokens=5 delims= " %%i in ('netstat -aon ^| findstr "%port%"') do @echo %%i >temp.txt
if exist temp.txt goto check
echo %servicename% start,Please wait...
start /b java %JAVA_OPTS% -jar %service% 
rem >> .\log\outer-%date:~0,4%%date:~5,2%%date:~8,2%.log 2>&1
goto end
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
:status
cls
netstat -aon | findstr "%port%"
rem for /f "tokens=5 delims= " %i in ('netstat -aon | findstr "%port%"') do set count=%i
for /f "tokens=5 delims= " %%i in ('netstat -aon ^| findstr "%port%"') do @echo %%i >temp.txt
if not exist temp.txt goto error
if exist temp.txt goto setpid
:setpid
set /p count=<temp.txt
echo Your %servicename% has been started,The Pids is %count%
pause
goto end
:check
cls
echo "%servicename% has been started."
pause
goto end
:error
cls
echo "%servicename%" has not start,Please check.
pause
:end