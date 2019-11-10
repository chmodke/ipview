package org.chmodke.ipview.buis.ip.utils;

import java.io.IOException;
import java.net.InetAddress;
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

    public static boolean ping(String ipAddress, int timeout) throws IOException {
        return InetAddress.getByName(ipAddress).isReachable(timeout);
    }

    public static boolean ping(String ipAddress) throws IOException {
        return ping(ipAddress, 1000);
    }

    public static void main(String[] args) {
        String ipAddress = "192.168.12.64";
        int ipInt = toInt(ipAddress);
        System.out.println(ipAddress + "=" + ipInt);

        String ipAddress2 = toIpAddress(ipInt);
        System.out.println(ipInt + "=" + ipAddress2);
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
