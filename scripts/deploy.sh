#!/bin/bash

APP_NAME=mountclim
REPOSITORY=/home/ec2-user

JAR_PATH=$(ls $REPOSITORY/*.jar | grep jar | head -n 1)

echo ">>>> JAR_PATH $JAR_PATH"

CURRENT_PID=$(pgrep -f $APP_NAME)
if [ -z $CURRENT_PID ]
then
  echo ">>>> java process not found."
else
  echo ">>>> PID: $CURRENT_PID kill."
  kill -15 $CURRENT_PID
  sleep 15
fi

echo ">>>> $JAR_PATH java execute."
nohup java -jar $JAR_PATH --spring.config.location=classpath:/application.yaml > /dev/null 2> /dev/null < /dev/null &
sleep 5
CURRENT_PID=$(pgrep -f $APP_NAME)
echo ">>>> New PID: $CURRENT_PID"