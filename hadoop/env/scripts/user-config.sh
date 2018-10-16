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




su - hadoop

cat <<EOT >>  $HOME/.bashrc
export JAVA_HOME=$(dirname $(dirname $(readlink -f $(which javac))))
export HADOOP_VERSION=\${HADOOP_VERSION}
export HADOOP_HOME=/usr/local/hadoop
export HADOOP_CONF_DIR=$HADOOP_HOME/etc/hadoop
export PATH=\$PATH:\$HADOOP_HOME/bin:\$HADOOP_HOME/sbin
export HADOOP_MAPRED_HOME=\${HADOOP_HOME}
export HADOOP_COMMON_HOME=\${HADOOP_HOME}
export HADOOP_HDFS_HOME=\${HADOOP_HOME}
export YARN_HOME=\${HADOOP_HOME}

EOT

echo -e  'y\n'|ssh-keygen -q -t rsa -N "" -f ~/.ssh/id_rsa
cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys

chmod go-w $HOME $HOME/.ssh
chmod 600 $HOME/.ssh/authorized_keys
chown `whoami` $HOME/.ssh/authorized_keys

echo 'map-red directories'
mkdir -p $HOME/workspace/dfs/name
mkdir -p $HOME/workspace/dfs/data


echo 'map-red directories'
mkdir -p $HOME/workspace/mapred/system
mkdir -p $HOME/workspace/mapred/local









