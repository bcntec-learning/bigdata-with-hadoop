#!/usr/bin/env bash

export HIVE_HOME=/usr/local/hive
export HIVE_BASE=apache-hive-2.3.3-bin

wget http://apache.rediris.es/hive/hive-2.3.3/$HIVE_BASE.tar.gz
tar -zxf $HIVE_BASE.tar.gz  -C /tmp
mv /tmp/$HIVE_BASE $HIVE_HOME
chown hadoop:hadoop -R $HIVE_HOME
rm $HIVE_BASE.tar.gz


sudo echo "export HIVE_HOME='HIVE_HOME'" >> /etc/profile.d/HIVE_HOME.sh
cat <<EOT >>  $HOME/.bashrc
export PATH=\$PATH:\$HIVE_HOME/bin:
EOT



