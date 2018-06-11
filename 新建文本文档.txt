
### 开发环境

| 工具    | 版本或描述                |
| ----- | -------------------- |
| OS    | Windows 10            |
| JDK   | 1.8+                 |
| IDE   | IntelliJ IDEA 2017.3 |
| Maven | 3.3.1                |
| MySQL | 5.6.4                |

### 模块划分

| 模块         | 释义                      |
| ---------- | ----------------------- |
| blog-core  | 核心业务类模块，提供基本的数据操作、工具处理等 |
| blog-admin | 后台管理模块                  |
| blog-web   | 前台模块                    |


### 技术栈

- Springboot 1.5.9
- Apache Shiro 1.2.2
- Logback
- Redis
- Lombok
- Websocket
- MySQL、Mybatis、Mapper、Pagehelper
- Freemarker
- Bootstrap 3.3.0
- wangEditor
- jQuery 1.11.1、jQuery Lazyload 1.9.7、fancybox、iCheck
- 阿里云OSS
- kaptcha
- Qiniu
- ...


### 使用方法

1. 使用IDE导入本项目
2. 新建数据库`CREATE DATABASE dblog;`
3. 导入数据库`docs/db/dblog.sql`
4. 修改(`resources/application.yml`)配置文件
   1. 数据库链接属性(可搜索`datasource`或定位到L.19) 
   2. redis配置(可搜索`redis`或定位到L.69)
   3. mail配置(可搜索`mail`或定位到L.89)
5. 运行项目(三种方式)
   1. 项目根目录下执行`mvn -X clean package -Dmaven.test.skip=true`编译打包，然后执行`java -jar target/blog-web.jar`
   2. 项目根目录下执行`mvn springboot:run`
   3. 直接运行`BlogWebApplication.java`
6. 浏览器访问`http://127.0.0.1:8443`


**后台用户**

_超级管理员_： 账号：root  密码：123456  （本地测试使用这个账号，admin没设置权限）

_普通管理员_： 账号：admin  密码：123456

_评论审核管理员_： 账号：comment-admin  密码：123456

注：后台用户的创建，尽可能做到**权限最小化**


### 更新日志

2018-05-25

**修改功能：**

1. 修复后台标签等分页失败的问题
2. 修复前台自动申请友链失败的问题
3. 其他一些问题


2018-05-22

**修改功能：**

1. 完善shiro权限（数据库、页面）。注：需要重新执行下`sys_resources`和`sys_role_resources`两张表的`insert`语句
2. redis配置默认不含密码（鉴于大多数朋友的redis都没有密码做此修改，不过本人 **强烈建议**设置下密码）

2018-05-18

**修复bug：**

1. web端自动申请友链后不显示的问题
2. config表修改后不能实时刷新的问题
	
**增加功能：**
1. 网站赞赏码
2. 百度推送功能(链接提交到百度站长平台)
	
**修改功能：**
1. 百度api的ak和百度推送的token以及七牛云的配置改为通过config表管理
3. admin模块菜单通过标签实时获取
3. 弹窗工具类js结构调整







