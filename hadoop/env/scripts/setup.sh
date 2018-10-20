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


su -
cat <<EOT >>  /etc/sysctl.conf
net.ipv6.conf.all.disable_ipv6 = 1
net.ipv6.conf.default.disable_ipv6 = 1
net.ipv6.conf.lo.disable_ipv6 = 1
EOT


echo "downloading hadoop" ${HADOOP_VERSION}
curl -sO http://apache.rediris.es/hadoop/common/hadoop-${HADOOP_VERSION}/hadoop-${HADOOP_VERSION}.tar.gz
tar -xzf hadoop-${HADOOP_VERSION}.tar.gz
mv hadoop-${HADOOP_VERSION} /usr/local/
mv /usr/local/hadoop-${HADOOP_VERSION} /usr/local/hadoop
rm  hadoop-${HADOOP_VERSION}.tar.gz

cp /home/vagrant/install/resources/* /usr/local/hadoop/etc/hadoop


echo "installing hadoop user"

useradd -d /home/hadoop -m hadoop
echo -e "H4d00p\nH4d00p\n" | sudo passwd hadoop
usermod -aG sudo hadoop
usermod -s /bin/bash hadoop
mkdir /tmp/hadoop-namenode
mkdir /tmp/hadoop-logs
mkdir /tmp/hadoop-datanode
chown -Rf hadoop:hadoop /usr/local/hadoop /tmp/hadoop-*







