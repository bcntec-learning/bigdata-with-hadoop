#!/usr/bin/env bash


if [  -f env.sh ]; then
    . env.sh
fi



if [[ -z $HADOOP_VERSION ]]; then
    echo "HADOOP_VERSION required"
    exit
fi

if [[ -z HADOOP_HOME ]]; then
    echo "HADOOP_HOME required"
    exit
fi

hdfs namenode -format

start-dfs.sh

sleep 10

start-yarn.sh
