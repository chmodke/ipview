package org.chmodke.ipview.utils;

import org.chmodke.ipview.buis.BuisConst;
import org.chmodke.ipview.buis.ip.DB;
import org.chmodke.ipview.buis.ip.utils.IpV4Util;
import org.junit.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

/****************************************************************  
 * <p>Filename:    TestIpUtil.java 
 * <p>Description:	 
 * <p>
 * <p>Create at:   2019/11/13 22:41
 * <p> 
 * <p>Modification History:  
 * <p>Date          Author  Version   Description  
 * <p>------------------------------------------------------------------  
 * <p>2019/11/13    kehao     1.0        
 * <p>------------------------------------------------------------------
 *
 * @author kehao
 * @version 1.0
 * @since
 *******************************************************************/

public class TestIpUtil {
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    @Test
    public void test() {
        int a = IpV4Util.toInt("10.135.125.148");
        System.out.println(a);
        //a=a+200;
        System.out.println(IpV4Util.toIpAddress(a));
    }

    @Test
    public void test3() throws IOException {
        IpV4Util.ping("10.135.125.94", 500);
    }

    @Test
    public void test4() {
        IpV4Util.pingCmd("192.168.100.96", 500);
        new Thread() {
            @Override
            public void run() {
                System.out.println(IpV4Util.getHostName("192.168.100.96"));
                System.out.println(IpV4Util.getCanonicalHostName("192.168.100.96"));
            }
        }.start();
        IpV4Util.pingCmd("192.168.100.100", 500);
        new Thread() {
            @Override
            public void run() {
                System.out.println(IpV4Util.getHostName("192.168.100.100"));
                System.out.println(IpV4Util.getCanonicalHostName("192.168.100.100"));
            }
        }.start();
    }

    /**
     * 计算网络等待时长，评估线程池配置
     */
    @Test
    public void test5() {
        int start = IpV4Util.toInt("192.168.100.101");
        long t1 = System.currentTimeMillis();
        System.out.println(t1);
        String ipAddress = IpV4Util.toIpAddress(start);
        HashMap<String, String> ip = new HashMap<String, String>();
        ip.put(DB.IP_ADDRESS, ipAddress);
        ip.put("LAST_UP_TIME", dtf.format(LocalDateTime.now()));
        long t2 = System.currentTimeMillis();
        System.out.println(t2);
        boolean a = IpV4Util.pingCmd(ipAddress, 500);
        long t3 = System.currentTimeMillis();
        System.out.println(t3);
        if (a) {
            ip.put("STATUS", BuisConst.STATUS_ALIVE);
            ip.put("HOSTNAME", IpV4Util.getHostName(ip.get(DB.IP_ADDRESS)));
        } else {
            ip.put("STATUS", BuisConst.STATUS_DEAD);
            ip.put("HOSTNAME", "N/A");
        }
        DB.updateIpTable(ip);
        long t4 = System.currentTimeMillis();
        System.out.println(t4);
        System.out.println("--------------------------");
        System.out.println("total:" + (t4 - t1));
        System.out.println("ping wait:" + (t3 - t2));
        System.out.println("--------------------------");
        System.out.println("local:" + (t4 - t1 - (t3 - t2)));
        System.out.println("--------------------------");

        int coreSise = Runtime.getRuntime().availableProcessors();


        long y = (t3 - t2);
        long x = ((t4 - t1) - y);

        int threadCount = (int) Math.floor(coreSise * (x + y) / x);
        System.out.println("threadCount:" + threadCount);

        //算出一个线程中，cpu时间（x）占整个线程工作时间（x+y）的比率（x/（x+y）），然后N核服务器为了N核都跑满100%那么就用N除以这个比率，最后加上1
        //N*((x+y)/x)+1
    }

    @Test
    public void test6() {
        System.out.println(IpV4Util.getHostName("192.168.100.1"));
        System.out.println(IpV4Util.getHostNameCmd("192.168.100.1"));
    }
}
