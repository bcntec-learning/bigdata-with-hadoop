#!/usr/bin/env bash

if [ -z $HADOOP_VERSION ]; then
    export HADOOP_VERSION=2.9.1
fi

if [ -z HADOOP_HOME ]; then
    export HADOOP_HOME=/usr/local/hadoop
fi