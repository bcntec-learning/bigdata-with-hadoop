#!/usr/bin/env bash
docker run -p 9092:9092 -p 2181:2181 \
    --name bigdata_kafka \
    -e KAFKA_ADVERTISED_HOST_NAME=192.168.0.100 \
    bigdata/kafka

