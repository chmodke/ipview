## About



> ipview局域网机器扫描工具

### 运行时依赖

- Windows

  ping、nbtstat

  > ` nbtstat -A 192.168.100.1 | findstr 唯一| findstr "<00>"`

- Linux

  ping、nmblookup

  > nmblookup来自于samba应用，管理员执行` yum install samba-client`进行软件安装
  >
  > ` nmblookup -A 192.168.100.1 | grep '<00>' | grep -v GROUP | awk '{print $1}'`

### 异常现象说明

- 多次观察存活主机检测为离线

  win10系统安全策略默认会禁止ICMP协议，导致无法通过ICMP协议判断存活状态.

  解决办法：

  - Windows管理员执行命令` netsh firewall set icmpsetting 8`设置允许ICMP入站。

  - Linux管理员执行命令

     ``` shell
     echo 0 >/proc/sys/net/ipv4/icmp_echo_ignore_all
     iptables -A INPUT -p icmp --icmp-type echo-request -j ACCEPT
     iptables -A OUTPUT -p icmp --icmp-type echo-reply -j ACCEPT
     ```

- 主机已上线但检测为离线

  应用后台扫描进程运行时间有间隔，待下一次执行扫描观察结果

