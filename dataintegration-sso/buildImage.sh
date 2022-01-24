#!/bin/sh
mvn clean deploy -DskipTests
server_name=dp-common-sso
server_version=v1.0
docker_registry=127.0.0.1:5000
docker rmi $docker_registry/$server_name:$server_version
docker rmi $server_name:$server_version
docker build -t $server_name:$server_version .
docker tag $server_name:$server_version  $docker_registry/$server_name:$server_version
docker push $docker_registry/$server_name:$server_version