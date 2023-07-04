# 项目简介

书山雅客是一款基于springboot与Vue 的仿起点中文网与笔趣阁的前后端分离的小说网站。



系统主要由小说首页、作家后台系统组成

目前主要功能为：

小说推荐

小说榜单

小说检索

小说排行榜

小说评论

作家功能：

小说发布

章节修改与新增





正在开发中功能

- [ ] 充值系统、稿费结算
- [ ] 排行榜排序算法
- [ ] 反馈系统
- [ ] 用户功能
- [ ] 章节评论
- [ ] 1.书架系统
- [ ] 2.阅读历史
- [ ] 3.充值消费记录
- [ ] 作家系统
- [ ] 1.薪酬查询
- [ ] 2.小说信息修改
- [ ] 3.章节顺序

- [ ] 后台管理系统:

- [ ] 1.作家管理

- [ ] 2.作品审核
- [ ] 3.首页管理 删除作品，推荐作品、限免管理
- [ ] 4.系统管理
- [ ] 5.评论管理
- [ ] 6.小说章节管理
- [ ] 7.订单管理、充值订单、订阅订单
- [ ] 8.统计报表： 会员、作家、小说、交易等数据的统计报表
- [ ] 9.新闻系统

其中小说推荐分为 首页轮播图、首页推荐、本周强推、热门推荐、精品推荐

小说榜单分为：点击榜单、新书榜单、更新榜单

小说首页显示小说推荐、小说榜单、新闻资讯与最近更新模块，（限免等模块因为未实现充值模块暂未实现），

作家管理模块中，用户需要经审核才能成为作家（未实现），并对自己的作品进行管理，包括新建作品、章节新增与修改等。



## 环境依赖

本项目基本使用docker部署，强烈建议小伙伴们使用docker,贴近原生的同时更隔离更便捷

- 腾讯云轻量级服务器 2核 4GB
- IntelliJ IDEA 2022.3（可选）
- VS Code

- MySQL 8.0.31
- Redis 7.3
- Elasticsearch 8.2.0
- RabbitMQ 3.10.2
- XXL-JOB 2.3.1
- JDK 17
- Maven 3.6.1
- Node 16.14
- MyBatis-Plus	3.5.3
- Spring Boot  3.0.0
- JJWT	0.11.5
- Caffeine	3.1.0
- Redisson	3.17.4
- Sentinel	1.8.4
- Springdoc-openapi  2.0.0
- Vue.js	3.2.13
- Vue Router	4.0.15
- axios	0.27.2
- element-plus	2.2.0



## 项目结构

```
cn
 +- com
     +- emirage   
        +- novel
            +- NovelApplication.java -- 项目启动类
            |
            +- core -- 项目核心模块，包括各种工具、配置和常量等
            |   +- common -- 业务无关的通用模块
            |   |   +- exception -- 通用异常处理
            |   |   +- constant -- 通用常量   
            |   |   +- req -- 通用请求数据格式封装，例如分页请求数据  
            |   |   +- resp -- 接口响应工具及响应数据格式封装 
            |   |   +- util -- 通用工具   
            |   | 
            |   +- auth -- 用户认证授权相关
            |   +- config -- 业务相关配置
            |   +- constant -- 业务相关常量         
            |   +- filter -- 过滤器 
            |   +- interceptor -- 拦截器
            |   +- json -- JSON 相关的包，包括序列化器和反序列化器
            |   +- task -- 定时任务
            |   +- util -- 业务相关工具 
            |   +- wrapper -- 装饰器
            |
            +- dto -- 数据传输对象，包括对各种 Http 请求和响应数据的封装
            |   +- req -- Http 请求数据封装
            |   +- resp -- Http 响应数据封装
            |
            +- dao -- 数据访问层，与底层 MySQL 进行数据交互
            +- manager -- 通用业务处理层，对第三方平台封装、对 Service 层通用能力的下沉以及对多个 DAO 的组合复用 
            +- service -- 相对具体的业务逻辑服务层  
            +- controller -- 主要是处理各种 Http 请求，各类基本参数校验，或者不复用的业务简单处理，返回 JSON 数据等
            |   +- front -- 小说门户相关接口
            |   +- author -- 作家管理后台相关接口
            |   +- admin -- 平台管理后台相关接口

```







## 数据库结构

数据库是一个项目的核心，考虑到未来的可扩展性，以及业务的使用情况，本项目数据库**核心**结构如下：

![](https://emirage-picgo.oss-cn-beijing.aliyuncs.com/image/novel-%E7%AC%AC%202%20%E9%A1%B5.jpg)

仍待完善内容：

后台管理系统：

- [ ] 用户与角色对应关系表
- [ ] 角色表
- [ ] 角色与菜单对应关系表
- [ ] 后台管理用户表
- [ ] 系统菜单表

## 开发过程

开发过程比较繁琐，所以将一些重要的，值得记录的部分记录进了我的博客当中

http://blog.emirage.com.cn/
