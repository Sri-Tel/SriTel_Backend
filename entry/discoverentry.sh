#!/bin/sh

while ! nc -z config-server 8888 ; do
    echo "Waiting for the Config Server"
    sleep 3
done


java -jar /home/app/sritel.jar
