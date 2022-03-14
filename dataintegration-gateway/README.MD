# 网关服务
平台网关服务，提供接口转发、访问认证、限流等功能

# 待开发
- 限流
- 权限认证


# docker服务启动-开发环境

## 注册中心

docker  run \
--name nacos-dev -d \
-p 8850:8848 \
--privileged=true \
--restart=always \
-e JVM_XMS=256m \
-e JVM_XMX=1g \
-e MODE=standalone \
-e PREFER_HOST_MODE=hostname \
nacos/nacos-server:1.1.4

# 分组管理

docker run -e spring.profiles.active=dev \
    -e spring.cloud.nacos.config.server-addr=192.168.124.170:8850 \
    -e spring.cloud.nacos.discovery.server-addr=192.168.124.170:8850 \
    -d dp-common-group-dev:v1.0
