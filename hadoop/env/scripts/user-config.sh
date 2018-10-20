#!/usr/bin/env bash



if [  -f env.sh ]; then
    . env.sh
fi


cat <<EOT >>  $HOME/.bashrc
export PATH=\$PATH:\$HADOOP_HOME/bin:\$HADOOP_HOME/sbin
EOT

echo -e  'y\n'|ssh-keygen -q -t rsa -N "" -f ~/.ssh/id_rsa
sleep 2

cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys

chmod go-w $HOME $HOME/.ssh
chmod 600 $HOME/.ssh/authorized_keys
chown `whoami` $HOME/.ssh/authorized_keys

echo 'dfs directories'
mkdir -p $HOME/workspace/dfs/name
mkdir -p $HOME/workspace/dfs/data


echo 'map-red directories'
mkdir -p $HOME/workspace/mapred/system
mkdir -p $HOME/workspace/mapred/local


sudo chown  -R hadoop:hadoop /usr/local/hadoop
sudo chown  -R hadoop:hadoop $HOME/workspace/









