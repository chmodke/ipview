package org.chmodke.ipview.buis.ip.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/****************************************************************  
 * <p>Filename:    IpV4Util.java 
 * <p>Description:	 
 * <p>
 * <p>Create at:   2019/11/8 23:11
 * <p> 
 * <p>Modification History:  
 * <p>Date          Author  Version   Description  
 * <p>------------------------------------------------------------------  
 * <p>2019/11/8    kehao     1.0        
 * <p>------------------------------------------------------------------
 *
 * @author kehao
 * @version 1.0
 * @since
 *******************************************************************/

public class IpV4Util {
    private static final Logger logger = LoggerFactory.getLogger(IpV4Util.class);
    /**
     * ip对应的正则表达式
     */
    private static final Pattern IP_PATTERN =
            Pattern.compile("(2(5[0-5]{1}|[0-4]\\d{1})|[0-1]?\\d{1,2})(\\.(2(5[0-5]{1}|[0-4]\\d{1})|[0-1]?\\d{1,2})){3}");

    /**
     * 将ipV4地址转换为对应的int值
     *
     * @param ipAddr ip地址(如192.168.12.11)
     * @return 转换为int值
     */
    public static int toInt(String ipAddr) {
        if (!IP_PATTERN.matcher(ipAddr).matches()) {
            throw new InvalidParameterException("IP地址不合法");
        }

        byte[] integers = toIntArray(ipAddr);

        return IntToByteArray.bytesToInt(integers);
    }

    /**
     * 将int值转换为对应的ip地址
     *
     * @param ipInteger ip地址的int值
     * @return 转换为ip地址(如192.168.12.11)
     */
    public static String toIpAddress(int ipInteger) {
        String[] str = new String[4];
        byte[] bytes = IntToByteArray.intToBytes(ipInteger);

        for (int i = 0; i < bytes.length; i++) {
            str[i] = Integer.toString(bytes[i] & 0xFF);
        }

        return Arrays.stream(str).collect(Collectors.joining("."));
    }

    /**
     * 将ipV4地址根据点号分割为4部分,每部分用byte表示
     *
     * @param ipAddr ipV4地址如 192.168.11.2
     * @return 返回[-64,-88,11,2] 因为 java的byte只能表示 -128~127,所以大于127的值被转换为负数
     */
    private static byte[] toIntArray(String ipAddr) {
        String[] split = ipAddr.split("\\.");
        byte[] result = new byte[4];

        for (int i = 0; i < split.length; i++) {
            result[i] = (byte) (Short.parseShort(split[i]));
        }

        return result;
    }

    public static boolean ping(String ipAddress, int timeout) {
        if (logger.isDebugEnabled())
            logger.debug("send ping to {},timeout is {}", ipAddress, timeout);
        try {
            InetAddress inet = InetAddress.getByName(ipAddress);
            return (null != inet && inet.isReachable(timeout))
                    || pingCmd(ipAddress, timeout);
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean ping(String ipAddress) {
        return ping(ipAddress, 1000);
    }

    public static boolean pingCmd(String ipAddress, int timeout) {
        String[] cmdCfg = null;
        if (timeout < 1000) {
            timeout = 1000;
        }
        if (OSUtils.WINDOWS) {
            //windows下-w是每次接收响应包的超时，单位是毫秒，但是我觉得是秒，
            String cmd = String.format("ping -w %s -n 4 %s", 4 * timeout / 1000, ipAddress);
            cmdCfg = new String[]{"cmd", "/c", cmd};
        } else if (OSUtils.LINUX) {
            //linux下-w每次接收响应包的超时时间，单位是秒，
            String cmd = String.format("ping -w %s -c 4 %s", 4 * timeout / 1000, ipAddress);
            cmdCfg = new String[]{"/bin/bash", "-c", cmd};
        }
        try {
            if (logger.isDebugEnabled())
                logger.debug("IpV4Util.pingCmd->cmd:{}", cmdCfg[2]);
            Process process = Runtime.getRuntime().exec(cmdCfg);
            int retVal = process.waitFor();
            if (logger.isDebugEnabled())
                logger.debug("IpV4Util.pingCmd resp->ip {}: retVal:{}", ipAddress, retVal);
            return retVal == 0 ? true : false;
        } catch (IOException e) {
            return false;
        } catch (InterruptedException e) {
            return false;
        }
    }

    public static boolean pingCmd(String ipAddress) {
        return pingCmd(ipAddress, 1000);
    }

    public static String getHostName(String ipAddress) {
        if (logger.isDebugEnabled())
            logger.debug("get hostname {}", ipAddress);
        try {
            InetAddress inet = InetAddress.getByName(ipAddress);
            String hostname = inet.getHostName();
            if (StringUtils.equals(hostname, ipAddress)) {
                hostname = getHostNameCmd(ipAddress);
            }
            return hostname;
        } catch (UnknownHostException e) {
            return "UnknownHost";
        }
    }

    public static String getCanonicalHostName(String ipAddress) {
        if (logger.isDebugEnabled())
            logger.debug("get hostname {}", ipAddress);
        try {
            InetAddress inet = InetAddress.getByName(ipAddress);
            return inet.getCanonicalHostName();
        } catch (UnknownHostException e) {
            return "UnknownHost";
        }
    }

    public static String getHostNameCmd(String ipAddress) {
        if (logger.isDebugEnabled())
            logger.debug("get hostname cmd {}", ipAddress);
        String hostname = "UnknownHost";
        String[] cmdCfg = null;

        if (OSUtils.WINDOWS) {
            String cmd = String.format("nbtstat -A %s | findstr 唯一| findstr \"<00>\"", ipAddress);
            cmdCfg = new String[]{"cmd", "/c", cmd};
        } else if (OSUtils.LINUX) {
            String cmd = String.format("nmblookup -A %s | grep '<00>' | grep -v GROUP | awk '{print $1}'", ipAddress);
            cmdCfg = new String[]{"/bin/bash", "-c", cmd};
        } else {
            return hostname;
        }

        BufferedReader bufferedreader = null;
        try {
            if (logger.isDebugEnabled())
                logger.debug("IpV4Util.getHostNameCmd->cmd:{}", cmdCfg[2]);
            Process process = Runtime.getRuntime().exec(cmdCfg, null);
            process.waitFor(5, TimeUnit.SECONDS);
            bufferedreader = new BufferedReader(new InputStreamReader(process.getInputStream(), OSUtils.osCharset));
            if (null != bufferedreader) {
                if (OSUtils.WINDOWS) {
                    for (String line = bufferedreader.readLine(); StringUtils.isNotBlank(line); line = bufferedreader.readLine()) {
                        hostname = (line.substring(0, (line.indexOf("<00>")))).trim();//截取字符串
                    }
                } else if (OSUtils.LINUX) {
                    String line = bufferedreader.readLine();
                    if (StringUtils.isNotBlank(line)) {
                        hostname = line.trim();
                    }
                }
            }
        } catch (IOException e) {
            logger.warn("get host :{} timeout", ipAddress);
        } catch (InterruptedException e) {
            //不会发生
        } catch (Exception e) {
            logger.error("IpV4Util.getHostNameCmd->ip: {} Exception:{}", ipAddress, e);
        } finally {
            try {
                if (null != bufferedreader) {
                    bufferedreader.close();
                }
            } catch (IOException e) {
                logger.error("close bufferedreader fail :{}", ipAddress);
            }
        }
        if (logger.isDebugEnabled())
            logger.debug("IpV4Util.getHostNameCmd resp->ip {}: hostname:{}", ipAddress, hostname);
        return hostname;
    }
}

class IntToByteArray {
    public static int bytesToInt(byte[] b) {
        return b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    public static byte[] intToBytes(int a) {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }
}
