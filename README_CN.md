<p align="center">
  <strong>基于kettle的可视化数据集成平台</strong>
</p>

<p align="center">
  <a href="https://github.com/young-datafan/data-integration/blob/develop/README.md">English</a> | 简体中文
</p>

<p align="center">
    <a target="_blank" href="https://github.com/young-datafan/data-integration/blob/develop/LICENSE">
        <img src="https://img.shields.io/badge/License-Apache%202.0-blue.svg?label=license" />
    </a>
    <a target="_blank" href="https://www.oracle.com/technetwork/java/javase/downloads/index.html">
        <img src="https://img.shields.io/badge/JDK-8+-green.svg" />
    </a>
</p>
<br/>

--------------------------------------------------------------------------------

# 架构

 ![](./docs/img/di-framework.png)  

--------------------------------------------------------------------------------

# 模块

 * dataintegration-common : 公共模块

 * dataintegration-group : 分组管理

 * dataintegration-project : 脚本管理
 
 * dataintegration-run : 数据集成运行模块
  
 * dataintegration-sso : sso单点登录模块
  
 * dataintegration-sys : 系统管理模块

 * dataintegration-vue : 前端vue模块

--------------------------------------------------------------------------------

# 功能特点

   * 提供了诸如限流、熔断、转发 、重写、重定向、和路由监控等插件；
   * 支持 HTTP、RESTFul、WebSocket、Dubbo、 GRPC、 Tars、 Spring Cloud 代理；
   * 支持热插拔，用户可以定制化开发；
   * 为了灵活的适配，选择器和规则可以动态的适配；
   * 支持集群部署；
   * 支持 A/B 测试和灰度发布。

--------------------------------------------------------------------------------

# 用户界面截图




--------------------------------------------------------------------------------

# 近期研发计划

TODO 

--------------------------------------------------------------------------------

# 参与贡献

非常欢迎大家来参与贡献，贡献流程请参考：
TODO

--------------------------------------------------------------------------------

# 快速试用 Docker

TODO

--------------------------------------------------------------------------------

# 如何构建

```bash
./mvnw clean install
```

--------------------------------------------------------------------------------

# 获得帮助

1. 提交[issue](https://github.com/young-datafan/data-integration/issues)

--------------------------------------------------------------------------------

## 版权

请参考 [LICENSE](https://github.com/young-datafan/data-integration/blob/develop/LICENSE) 文件.

--------------------------------------------------------------------------------
