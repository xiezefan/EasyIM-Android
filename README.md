# EasyIM-Android

EasyIM是一个移动即时通讯应用。是我的本科毕业设计。  
  
因为实习占用了大四的大部分时间，导致EasyIM只完成了部分功能便参与答辩。虽然最终侥幸过了毕设答辩，但效果与自己期望的相去甚远。  
  
开始工作后，有比较多的空闲时间，我打算慢慢完善该项目，并将近期所学习的一些新技术应用到该项目中。  


## 简述

该项目分为两部分：

* [EasyIM-Server][1] : EasyIM服务端
* [EasyIM-Android][2] : EasyIM Android端

EasyIM-Server主要完成一下功能：

* 用户基础功能
* 好友功能
* 点对点的消息发送功能
* 离线消息功能

### 技术与框架

* ButterKnife
* Picasso
* Retrofit
* GreenDao
* EventBus
* RxAndroid

PS:项目中使用了Lambda表达式，所以，请确保安装了JDK1.8及Gradle2.2以上。

## 效果图

![3]

[1]:https://github.com/xiezefan/EasyIM-Server
[2]:https://github.com/xiezefan/EasyIM-Android
[3]:http://7jpp6b.com1.z0.glb.clouddn.com/blog/easyim_all.jpg
