#!/usr/bin/env bash



if [  -f env.sh ]; then
    . env.sh
fi




su - hadoop

cat <<EOT >>  $HOME/.bashrc
export JAVA_HOME=$(dirname $(dirname $(readlink -f $(which javac))))
export HADOOP_VERSION=\${HADOOP_VERSION}
export HADOOP_HOME=/usr/local/hadoop
export HADOOP_CONF_DIR=/usr/local/hadoop/etc/hadoop
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









