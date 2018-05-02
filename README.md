SpringBoot实现微信公众号聊天机器人


微信API sdk : https://github.com/Wechat-Group/weixin-java-tools
智能聊天机器人API：http://api.qingyunke.com/


关于pom文件为什么使用8.0.29 版本的tomcat问题

Springboot 构建http服务，返回的http行是'HTTP/1.1 200'而非'HTTP/1.1 200 OK'

从Springboot 1.4.0以上版本就有这个问题

准确的说应该是tomcat的版本升级了。 http/1.1协议里 ok是被括号的。也就是可以有可以无。
客户端应该要兼容这种
所以采用降低springbot内置tomcat版本号的方式
