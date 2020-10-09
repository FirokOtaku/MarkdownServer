# Markdown文档服务器

目的是提供一个简易的Markdown文档查看方式.

## 功能特性

* Markdown文档管理
* 图片管理 (计划中)
* 登录控制 (计划中)
* 操作日志 (计划中)

## 详细介绍

调整`cmds.jar/BOOT-INF/classes/application.properties`配置文件中的`app.basePath`键即可更改管理的文档文件夹.

使用`java -jar docs.jar`启动服务器应用.

启动后, 在浏览器访问`ip:port/static/index.html`即可打开查看页面.
