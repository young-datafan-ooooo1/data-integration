#!/bin/sh
localIp=192.168.2.244

server_name=dp-common-sso
server_version=v1.0
docker_registry=127.0.0.1:5000


pid=`docker ps -a | grep $server_name | grep -v grep | awk '{print $1}'`
echo "旧容器id：$pid"
if [ -n "$pid" ]
then
docker stop $pid
docker rm -f $pid
fi

# 删除镜像
# docker rmi $docker_registry/$server_name:$server_version
# 启动容器
docker run --name $server_name  --restart=always -m 1g -e TZ="Asia/Shanghai" -e spring.profiles.active=local \
    -e spring.cloud.nacos.config.server-addr=$localIp:8848 \
    -e spring.cloud.nacos.discovery.server-addr=$localIp:8848 \
    -e spring.redis.host=$localIp \
    -e spring.redis.port=6379 \
    -e spring.cloud.nacos.discovery.ip=$localIp \
    -e spring.datasource.url=jdbc:mysql://$localIp:3306/dp\?useUnicode=true\&characterEncoding\=utf-8\&useSSL\=false \
    -p 10217:10217 \
    -d $docker_registry/$server_name:$server_version