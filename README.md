# javautils
各种常用的java逻辑代码封装的工具类

# 目录
- [获取本地ip](#NetworkUtils.java)

# NetworkUtils.java
获取本地ip地址工具类，多网卡的情况下获取全部ip地址
```java
getLocalLANIP() //通过网络接口获取：比较靠谱的方式
getFirstLocalHostIP() //通过主机名获取：获取第一个ip地址，具体逻辑看类说明，实际使用调用该方法获取ip即可
getLocalHostName() //获取主机名
getAllLocalHostIP() //获取所有主机
getInstance() //工具采用单例，获取实例方法
```
[NetworkUtils.java](NetworkUtils.java)