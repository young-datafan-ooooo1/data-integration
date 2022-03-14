# 系统管理
平台门户-系统管理模块，包含基本的用户管理、角色管理、资源管理、日志管理以及多租户等权限管理功能，提供平台的用户等资源管理能力。

# consul

下载地址：https://www.consul.io/downloads.html
mac：https://releases.hashicorp.com/consul/1.6.3/consul_1.6.3_darwin_amd64.zip

启动：
nohup ./consul agent -dev &


# feign

使用继承示例，引入dp-portal-system-management-api依赖
```
package com.youngdatafan.portal.system.management.client;

import com.youngdatafan.portal.system.management.user.api.UserServiceApi;

/**
 * @author gavin
 * @since 2020/2/7 12:14 下午
 */
@FeignClient("dp-portal-system-management-provider")
public interface UserServiceApiClient extends UserServiceApi {
}
```

# token示例

curl -X POST \
  http://localhost:10217/oauth/token \
  -d grant_type=password \
  -d client_id=dp-client \
  -d client_secret=dp-secret \
  -d username=user-username \
  -d password=123456
  

```shell
keytool -genkey -alias jwt -keyalg RSA -keysize 1024 -keystore jwt.jks -validity 3650 
输入：CN=dataintegration, OU=dataintegration, O=sensesai, L=shanghai, ST=shanghai, C=CN
--
使用"keytool -importkeystore -srckeystore jwt.jks -destkeystore jwt.jks -deststoretype pkcs12"  PKCS12
```
### 导出公钥 

```shell
keytool -list -rfc --keystore jwt.jks | openssl x509 -inform pem -pubkey -noout >> jwt.pub

```





