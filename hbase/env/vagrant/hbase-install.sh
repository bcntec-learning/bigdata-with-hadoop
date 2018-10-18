#!/usr/bin/env bash

export HBASE_HOME=/usr/local/hbase

wget http://apache.rediris.es/hbase/1.4.8/hbase-1.4.8-bin.tar.gz
tar -zxf hbase-1.4.8-bin.tar.gz -C /tmp
sudo mv /tmp/hbase-1.4.8 $HBASE_HOME
sudo mkdir /usr/local/zookeeper/
sudo chown hadoop:hadoop -R /usr/local/zookeeper/ $HBASE_HOME
rm hbase-1.4.8-bin.tar.gz


sudo cp hbase-site.xml /usr/local/hbase/conf
#sudo cp hbase-env.sh /usr/local/hbase/conf

echo "export HBASE_HOME='$HBASE_HOME'" >> /etc/profile.d/HBASE_HOME.sh

cat <<EOT >>  $HOME/.bashrc
export PATH=\$PATH:\$HBASE_HOME/bin:
EOT



