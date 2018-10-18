FROM ubuntu:trusty



ENV HADOOP_VERSION=2.9.1
ENV HADOOP_HOME=/usr/local/hadoop

ARG TAR=hadoop-$HADOOP_VERSION.tar.gz

ENV PATH $PATH:/usr/local/hadoop/bin
ENV DEBIAN_FRONTEND noninteractive

WORKDIR /





RUN sed -ie '/^XKBLAYOUT=/s/".*"/"es"/' /etc/default/keyboard && \
    udevadm trigger --subsystem-match=input --action=change && \
    dpkg-reconfigure keyboard-configuration -f noninteractive && \
    apt-get update && \
    apt-get autoremove -y && \
    apt-get install  -y --no-install-recommends ca-certificates software-properties-common && \
    add-apt-repository ppa:openjdk-r/ppa -y && \
    apt-get update && \
    apt-get -y install apache2 openjdk-8-jdk && \
    apt-get clean all && \
    rm -rf /var/lib/apt/lists/*


COPY ../scripts /scripts

RUN set -euxo pipefail && \
    /scripts/unpack.sh && \
    /scripts/unpack.sh && \
    chmod 0700 /root/.ssh


COPY entrypoint.sh /
COPY ../resources/core-site.xml /usr/local/hadoop/etc/hadoop/
COPY ../resources/hdfs-site.xml /usr/local/hadoop/etc/hadoop/
COPY ../resources/yarn-site.xml /usr/local/hadoop/etc/hadoop/
COPY ../resources/mapred-site.xml /usr/local/hadoop/etc/hadoop/
COPY ../resources/hadoop-env.sh /usr/local/hadoop/etc/hadoop/
COPY profile.d/hadoop.sh /etc/profile.d/
COPY ssh/config /root/.ssh/

EXPOSE 8020 8042 8088 9000 10020 19888 50010 50020 50070 50075 50090

CMD "/entrypoint.sh"