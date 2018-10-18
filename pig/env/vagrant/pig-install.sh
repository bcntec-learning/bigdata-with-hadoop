#!/usr/bin/env bash

export PIG_HOME=/usr/local/pig
export PIG_BASE=pig-0.17.0

wget https://www.apache.org/dist/pig/pig-0.17.0/$PIG_BASE.tar.gz
tar -zxf $PIG_BASE.tar.gz  -C /tmp
mv /tmp/$PIG_BASE $PIG_HOME
chown hadoop:hadoop -R $PIG_HOME
rm $PIG_BASE.tar.gz



sudo echo "export PIG_HOME='$PIG_HOME'" >> /etc/profile.d/PIG_HOME.sh
sudo echo "export PIG_CLASSPATH='\$HADOOP_CONF_DIR'" >> /etc/profile.d/PIG_CLASSPATH.sh
cat <<EOT >>  $HOME/.bashrc
export PATH=\$PATH:\$PIG_HOME/bin:
EOT



