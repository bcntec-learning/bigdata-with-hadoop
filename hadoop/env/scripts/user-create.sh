#!/usr/bin/env bash



. env.sh


echo "installing hadoop user"

sudo useradd -d /home/hadoop -m hadoop
echo -e "H4d00p\nH4d00p\n" | sudo passwd hadoop
sudo usermod -aG sudo hadoop
sudo usermod -s /bin/bash hadoop
mkdir /tmp/hadoop-namenode
mkdir /tmp/hadoop-logs
mkdir /tmp/hadoop-datanode
sudo chown -Rf hadoop:hadoop /usr/local/hadoop /tmp/hadoop-*


