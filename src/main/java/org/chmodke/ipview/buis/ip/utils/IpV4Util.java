package org.chmodke.ipview.buis.ip.utils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chmodke.ipview.buis.ip.DB;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.InvalidParameterException;
import java.util.Arrays;
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
    private static final Log logger = LogFactory.getLog(IpV4Util.class);
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
            logger.debug(String.format("send ping to %s,timeout is %s", ipAddress, timeout));
        try {
            return DB.getInetAddress(ipAddress).isReachable(timeout) || pingCmd(ipAddress, timeout);
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean ping(String ipAddress) {
        return ping(ipAddress, 1000);
    }

    public static boolean pingCmd(String ipAddress, int timeout) {
        if (logger.isDebugEnabled())
            logger.debug(String.format("send ping to %s,timeout is %s", ipAddress, timeout));
        String cmd = String.format("ping %s", ipAddress);
        if (OSUtils.WINDOWS) {
            //linux下-w单位是毫秒
            cmd = String.format("ping -w %s -n 2 %s", timeout, ipAddress);
        } else if (OSUtils.LINUX) {
            if (timeout < 1000) {
                timeout = 1000;
            }
            //linux下-w单位是秒
            cmd = String.format("ping -w %s -c 2 %s", timeout / 1000, ipAddress);
        }
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            int retVal = process.waitFor();
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
            logger.debug(String.format("get hostname %s", ipAddress));
        try {
            InetAddress inet = DB.getInetAddress(ipAddress);
            return inet.getHostName();
        } catch (UnknownHostException e) {
            return "UnknownHost";
        }
    }

    public static String getCanonicalHostName(String ipAddress) {
        if (logger.isDebugEnabled())
            logger.debug(String.format("get hostname %s", ipAddress));
        try {
            InetAddress inet = DB.getInetAddress(ipAddress);
            return inet.getCanonicalHostName();
        } catch (UnknownHostException e) {
            return "UnknownHost";
        }
    }

    public static void main(String[] args) {
        System.out.println(ping("10.135.125.148", 500));
        new Thread() {
            @Override
            public void run() {
                System.out.println(getHostName("10.135.125.148"));
                System.out.println(getCanonicalHostName("10.135.125.148"));
            }
        }.start();
        System.out.println(ping("10.135.125.150", 500));
        new Thread() {
            @Override
            public void run() {
                System.out.println(getHostName("10.135.125.150"));
                System.out.println(getCanonicalHostName("10.135.125.150"));
            }
        }.start();
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
