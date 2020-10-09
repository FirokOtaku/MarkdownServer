# Markdown文档服务器

提供一种快速分享Markdown文档的简易方式.

应用后端基于Spring Boot搭建; 前端是基于Vue的SPA.

变更日志 [链接](changelog.md)

> Markdown是好文化  
> 不会Markdown的面壁, 请  
> 顺便, 用Office写程序文档的都是邪教

## 功能特性

* Markdown文档管理
* 图片管理 (计划中)
* 操作控制 (计划中)
* 操作日志 (计划中)

## 详细介绍

### 如何使用

调整`mds.jar/BOOT-INF/classes/application.properties`配置文件中的`app.basePath`键即可更改管理的文档文件夹.

使用`java -jar docs.jar`启动服务器应用.

启动后, 在浏览器访问`ip:port/static/index.html`即可打开查看页面.

您可以通过应用内置简易编辑器/远程桌面/FTP等方式管理文件夹内容.

### 已知缺陷

* 目前版本( `[0.1.0,)` )没有使用数据库, 所有数据实时读写文件系统得到且没有进行缓存
* 目前版本( `[0.1.0,)` )没有操作控制, 所有获得应用访问地址的用户都可以对文档进行增删改操作
* 前端SPA所需静态资源没有使用CDN而是直接向服务端请求

由于上述原因, 本应用仅适用于小规模、本地的文档共享.

## 相关链接

* [Vue](https://vuejs.org)
* [Vue Router](https://router.vuejs.org)
* [axios.js](https://github.com/axios/axios)
* [Marked.js](https://marked.js.org)
* [Highlight.js](https://highlightjs.org)
* [Qs.js](https://github.com/ljharb/qs)
* [CSS样式集 - Templated - INDUSTRIOUS](https://templated.co/industrious)
* [Font Awesome中文网](http://www.fontawesome.com.cn)

> 不会吧不会吧不会吧, 还有人不会Markdown吗🤔

* [Markdown教程和语法](https://www.runoob.com/markdown/md-tutorial.html)
* [Markdown中文网](https://markdown.com.cn)
