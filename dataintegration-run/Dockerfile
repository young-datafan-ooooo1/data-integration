FROM openjdk:8-alpine

# poi 空指针问题
ENV LANG en_US.UTF-8
RUN apk add --update ttf-dejavu fontconfig && rm -rf /var/cache/apk/*
# 使普通用户也能运行
RUN mkdir /app && chmod 777 /app

# set environment
ENV JVM_XMS="2g" \
    JVM_XMX="2g" \
    JVM_DEBUG="n" \
    JVM_DEBUG_PORT="9555" \
    JVM_CUSTOM="" \
    JAR_FILE="./run.jar" \
    TIME_ZONE="Asia/Shanghai" \
    ENCODING="UTF-8"

# 添加启动jar
COPY dataintegration-run-management-provider/target/dataintegration-run-management-provider-1.0.0-SNAPSHOT-ark-executable.jar /app/run.jar
# 添加启动脚本
ADD docker-startup.sh /app/bin/docker-startup.sh
RUN chmod +x /app/bin/docker-startup.sh
# 切换到/app目录
WORKDIR /app

ENTRYPOINT ["./bin/docker-startup.sh"]
