#!/bin/bash
#outer.sh
service=outer-start-0.0.1-SNAPSHOT.jar
servicename="outer service"
port="1099"
JAVA_OPTS=""
PIDS=`ps -ef | grep "${service}" | grep -v grep | awk '{print $2}'`
cd ..
if [ ! -d "./log" ];then
mkdir log
fi
checkport()
{
sleep 5
for portnum in ${port}
do
netstat -an|grep ${portnum}
if [ $? -eq 0 ];then
echo "${servicename} port [${portnum}] has start sucessed." |tee -a ./log/outer-`date -d now +%Y-%m-%d`.log
else
echo "${servicename} port [${portnum}] start failed,Please check" |tee -a ./log/outer-`date -d now +%Y-%m-%d`.log && exit 1
fi
done
}
case $1 in
  'start')
        if [ "X$PIDS" != "X" ]; then
                echo "${servicename} has been started, the PID = "${PIDS}""
        else
        echo "${servicename} is startting..."
    java ${JAVA_OPTS} -jar ${service} >> ./log/outer-`date -d now +%Y-%m-%d`.log 2>&1&
    checkport
        fi 
		;;
  'restart')
        if [ "X$PIDS" != "X" ]; then
                kill -9 $PIDS
                echo "${servicename} has been killed, PID="${PIDS}"!"
        fi
        echo "${servicename} is startting..."
    java ${JAVA_OPTS} -jar ${service} >> ./log/outer-`date -d now +%Y-%m-%d`.log 2>&1&
    checkport 
	;;
  'status')
        if [ "X$PIDS" != "X" ]; then
                echo "${servicename} has been started, the PID = "${PIDS}""
        else
           echo "${servicename} has not been started"
        fi ;;
  'stop')
        if [ "X$PIDS" != "X" ]; then
                kill -9 $PIDS
                echo "${servicename} has been killed, PID="${PIDS}"!"
        else
                echo "${servicename} has not been started, the stop command do nothing."
        fi  ;;
  *) echo "Call parameters error"
     echo "Usage:${servicename} | start | restart | stop | status"
     exit 1 ;;
esac