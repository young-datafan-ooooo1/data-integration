
# openoffice 本地安装

## 修改app-cfg.js
```javascript
下载地址：http://www.openoffice.org/zh-cn/download/
下载后自动安装

vim ~/.zshrc   //有的可能是.bashrc文件
或者编辑器打开：open ~/.zshr

在.zshrc文件中增加 

export PATH=$PATH:/Applications/OpenOffice.app/Contents/MacOS

执行
source ~/.zshrc   //使环境变量立即生效
source ~/.bash_profile    (不知道用没，多执行一句不会错)
启动OpenOffice

soffice -headless -accept="socket,host=localhost,port=8100;urp;" -nofirststartwizard
（将以服务的方式启动OpenOffice，并监听本机的8100端口
```
## 引入依赖
```javascript
        <dependency>
            <groupId>org.jodconverter</groupId>
            <artifactId>jodconverter-core</artifactId>
            <version>${openoffice.version}</version>
        </dependency>

        <dependency>
            <groupId>org.jodconverter</groupId>
            <artifactId>jodconverter-spring-boot-starter</artifactId>
            <version>${openoffice.version}</version>
        </dependency>

        <!--jodconverter 本地支持包 -->
        <dependency>
            <groupId>org.jodconverter</groupId>
            <artifactId>jodconverter-local</artifactId>
            <version>${openoffice.version}</version>
        </dependency>
```
---
## yaml配置openoffice安装地址和端口信息
```javascript
jodconverter:
  local:
    enabled: true
    office-home: /Applications/OpenOffice.app/Contents/
    max-tasks-per-process: 10
    port-numbers: 8100
```
---
## 测试
```javascript

   // 第一步：转换器直接注入
    @Autowired
    private DocumentConverter converter;
    @Test
    public void officeTest() throws Exception {
        File file = new File("/Users/songxiaolang/漏洞修复报告.docx");//需要转换的文件
        try {
            //文件转化
            converter.convert(file).to(new File("/Users/songxiaolang/officetest/hello.pdf")).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

```


