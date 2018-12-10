#!/bin/sh
SHUTDOWN_WAIT=40
#pid=`jps|grep -v Jps|awk '{print $1}'`
JAVA_APP=`cd /dashu/application;ls fk-risk-test*.jar`
pid=`ps -ef |grep fk-risk-test|grep -v grep |awk '{print $2}'`
echo $pid
if [ -n "$pid" ]
then
        echo -e "\e[00;31mStoping java\e[00m"
        kill $pid
        let kwait=$SHUTDOWN_WAIT
        count=0;
        until  [ `ps -p $pid | grep -c $pid` = '0' ] || [ $count -gt $kwait ]
        do
                echo -n -e "\n\e[00;31mwaiting for processes to exit\e[00m";
                sleep 1
                let count=$count+1;
        done
        if [ $count -gt $kwait ]; then
                echo -n -e "\n\e[00;31mkilling -9 processes which didn't stop after $SHUTDOWN_WAIT seconds\e[00m"
                kill -9 $pid
        fi
fi
echo -n -e "\n\e[00;31mSTART PROCESS\e[00m\n";
echo $JAVA_HOME
JAVA_OPTS_MEM="-server -Xms512M -Xmx2048M "
JAVA_OPTS_GC="-XX:+DisableExplicitGC -XX:+PrintGC -XX:+PrintGCDetails -XX:+PrintGCTimeStamps
              -Xloggc:/dashu/log/gc.log -XX:GCLogFileSize=20M -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=20 "
JAVA_OPTS_ERROR="-XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/dashu/dump/ "
#JAVA_CMD="nohup $JAVA_HOME/bin/java $JAVA_OPTS_MEM $JAVA_OPTS_GC $JAVA_OPTS_ERROR -jar $JAVA_APP >/dev/null 2>&1  &"
echo "JAVA_OPTS_MEM:" $JAVA_OPTS_MEM
echo "JAVA_OPTS_GC:" $JAVA_OPTS_GC
echo "JAVA_OPTS_ERROR:" $JAVA_OPTS_ERROR
echo $JAVA_CMD
#su - root -c "$JAVA_CMD"
nohup $JAVA_HOME/bin/java $JAVA_OPTS_MEM $JAVA_OPTS_GC $JAVA_OPTS_ERROR -jar $JAVA_APP >/dev/null 2>&1  &
pid=`ps -ef |grep $JAVA_APP|grep -v grep |awk '{print $2}'`
if [ -n "$pid" ]
then
        echo $pid
        echo -e "\e[00;31mStart java success\e[00m"
else
        echo -e "\e[00;31mStart java failed\e[00m"
fi