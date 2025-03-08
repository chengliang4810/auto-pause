# 贝尔实验室 Spring 官方推荐镜像 JDK下载地址 https://bell-sw.com/pages/downloads/
FROM bellsoft/liberica-openjdk-debian:17.0.11-cds
#FROM docker.1panel.live/bellsoft/liberica-openjdk-debian:17.0.11-cds

LABEL maintainer="chengliang4810"

RUN mkdir -p /fsm/logs \
    /fsm/data

WORKDIR /fsm

ENV SERVER_PORT=8080 LANG=C.UTF-8 LC_ALL=C.UTF-8 JAVA_OPTS="" APITOKEN="" QBHOST="" QBUSERNAME="" QBPASSWORD=""

EXPOSE ${SERVER_PORT}

ADD ./target/fsm.jar ./app.jar

ENTRYPOINT java -Djava.security.egd=file:/dev/./urandom -Dserver.port=${SERVER_PORT} \
           -XX:+HeapDumpOnOutOfMemoryError -XX:+UseZGC ${JAVA_OPTS} \
           -jar app.jar
