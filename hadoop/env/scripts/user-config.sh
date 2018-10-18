#!/usr/bin/env bash




if [  -f env.sh ]; then
    . env.sh
fi




if [ -z $HADOOP_VERSION ]; then
    echo "HADOOP_VERSION required"
    exit
fi
if [ -z HADOOP_HOME ]; then
    echo "HADOOP_HOME required"
    exit
fi

echo 'adding envs keys'
cat <<EOT >>  $HOME/.bashrc
export PATH=\$PATH:\$HADOOP_HOME/bin:\$HADOOP_HOME/sbin
EOT

echo 'creating keys'
echo -e  'y\n'|ssh-keygen -q -t rsa -N "" -f ~/.ssh/id_rsa
cat ~/.ssh/id_rsa.pub >> ~/.ssh/authorized_keys

chmod go-w $HOME $HOME/.ssh
chmod 600 $HOME/.ssh/authorized_keys
chown `whoami` $HOME/.ssh/authorized_keys

echo 'creating map-red directories'
mkdir -p $HOME/workspace/dfs/name
mkdir -p $HOME/workspace/dfs/data


echo 'creating map-red directories'
mkdir -p $HOME/workspace/mapred/system
mkdir -p $HOME/workspace/mapred/local


echo 'done.. remove user-config.sh'









