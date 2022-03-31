#服务器ip
hostIp=$1
#项目编译打包
cd ../ && mvn clean package -DskipTests -Dcheckstyle.skip=true
#构建镜像
docker build -t dataintegration-group-provider dataintegration-group
docker build -t dataintegration-file-management-provider dataintegration-file-management
docker build -t dataintegration-gateway dataintegration-gateway
docker build -t dataintegration-model-management-provider dataintegration-model
docker build -t dataintegration-project-provider dataintegration-project
docker build -t dataintegration-run-management-provider dataintegration-run
docker build -t dataintegration-sso-provider dataintegration-sso
docker build -t dataintegration-sys-management-provider dataintegration-sys
docker build -t dataintegration-ui dataintegration-ui
#启动应用
cd install && sed -i "s,hostIp,${hostIp},g" docker-compose.yaml
docker-compose up -d