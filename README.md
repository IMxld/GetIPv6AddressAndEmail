# 关于本程序
使用前情确保你的设备拥有公网ipv6地址，否则只会获取到内网的ipv6地址

这个程序原本是写给hxd的家用服务器用的，他只有动态公网ipv6地址:(
# 关于配置文件
**添加邮箱**

记事本打开config.properties

另起一行行，写入

member【序号】=【邮箱】

并将memberNum的值+1

忘记加的话就只会给从上到下memberNum个邮箱发

例如，现在文件里是：
```
memberNum=1
member0=123456789@qq.com
```
添加好两个人后是：
```
memberNum=3
member0=123456789@qq.com
member1=987654321@qq.com
member2=1145141919@qq.com
```
**参数说明**
```
username：发送邮件用的邮箱
password：QQ邮箱-设置-账户-POP3/SMTP开启后得到的代码
getIPv6Interval：检测本机IPv6地址的时间间隔（单位秒）
sendMailInterval：给所有用户发送IPv6地址时的时间间隔（单位秒）
memberNum：要发送的人数
member[0123456789]*：用户编号
```
**注意事项**

接收邮箱请使用qq邮箱，特别强调是qq.com结尾的邮箱

foxmail没有测试过，理论上可以

java版本建议使用jre1.8

jdk11以上都不行（email模块的缘故）
# 其他事项
**参考指令**

Windows==>C:\Progra~1\Java\jre1.8.0_351\bin\java.exe -jar .\getIPv6Address.jar

Linux==>jar -jar ./getIPv6Address.jar

**获取java**

Windows：

如果压缩包里的那个【jre-8u351-windows-x64.exe】不能用

[这里请](https://www.java.com/zh-CN/download/)

Linux：

[参考文章](https://blog.csdn.net/wtxasdasd123/article/details/103883356)
