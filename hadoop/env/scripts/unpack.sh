#!/usr/bin/env bash
. env.sh


echo "downloading hadoop" ${HADOOP_VERSION}
curl -O http://apache.rediris.es/hadoop/common/hadoop-${HADOOP_VERSION}/hadoop-${HADOOP_VERSION}.tar.gz
tar -xvzf hadoop-${HADOOP_VERSION}.tar.gz
sudo mv hadoop-${HADOOP_VERSION} /usr/local/
sudo mv /usr/local/hadoop-${HADOOP_VERSION} /usr/local/hadoop
rm -v hadoop-${HADOOP_VERSION}.tar.gz

cp -v $HOME/install-resources/* /usr/local/hadoop/etc/hadoop

