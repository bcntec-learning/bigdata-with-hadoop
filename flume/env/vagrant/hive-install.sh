#!/usr/bin/env bash

export FLUME_HOME=/usr/local/flume
export FLUME_BASE=apache-flume-1.8.0-bin

wget http://www.apache.org/dyn/closer.lua/flume/1.8.0/apache-flume-1.8.0-bin.tar.gz
tar -zxf $FLUME_BASE.tar.gz  -C /tmp
mv /tmp/$FLUME_BASE $FLUME_HOME
chown hadoop:hadoop -R $FLUME_HOME
rm $FLUME_BASE.tar.gz


sudo echo "export FLUME_HOME='FLUME_HOME'" >> /etc/profile.d/FLUME_HOME.sh
cat <<EOT >>  $HOME/.bashrc
export PATH=\$PATH:\$FLUME_HOME/bin:
EOT



