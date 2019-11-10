package org.chmodke.ipview.buis.ip.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chmodke.ipview.buis.ip.DB;
import org.chmodke.ipview.buis.ip.utils.IpV4Util;
import org.chmodke.ipview.common.core.config.GlobalConfig;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimerTask;

/****************************************************************  
 * <p>Filename:    RefreshIpJob.java 
 * <p>Description:	 
 * <p>
 * <p>Create at:   2019/11/8 22:22
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

public class RefreshIpJob extends TimerTask {
    private static final Log logger = LogFactory.getLog(RefreshIpJob.class);

    private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void run() {
        try {
            logger.info("RefreshIpJob execut date " + formatter.format(Calendar.getInstance().getTime()));
            reFresh(GlobalConfig.getProperties("startIp"), Integer.parseInt(GlobalConfig.getProperties("scanLength")));
        } catch (Exception e) {
            logger.error("RefreshIpJob.run->Exception:", e);
        }
    }


    public static void reFresh(String startIp, int length) {

        ArrayList<HashMap<String, String>> ipTable = new ArrayList<HashMap<String, String>>();
        int start = IpV4Util.toInt(startIp);
        for (int i = 0; i <= length; i++) {

            HashMap<String, String> ip = new HashMap<String, String>();
            String ipAddress = IpV4Util.toIpAddress(start + i);
            ip.put(DB.IP_ADDRESS, ipAddress);
            boolean isAlive = false;
            try {
                if (logger.isDebugEnabled())
                    logger.debug(String.format("send ping:%s", ipAddress));
                InetAddress inet = InetAddress.getByName(ipAddress);
                isAlive = inet.isReachable(1000);
            } catch (IOException e) {
            }
            if (isAlive) {
                ip.put("STATUS", "Alive");
                if (logger.isDebugEnabled())
                    logger.debug(String.format("Address:%s is:%s", ipAddress, "Alive"));
            } else {
                ip.put("STATUS", "Dead");
                if (logger.isDebugEnabled())
                    logger.debug(String.format("Address:%s is:%s", ipAddress, "Dead"));
            }
            ip.put("LAST_UP_TIME", formatter.format(Calendar.getInstance().getTime()));
            ipTable.add(ip);
        }

        DB.clearIpTable();
        DB.updateIpTable(ipTable);
    }

    public static void reFresh(String startIp, String endIp) {
        int start = IpV4Util.toInt(startIp);
        int end = IpV4Util.toInt(endIp);
        int length = end - start;

        reFresh(startIp, length);
    }


    @Test
    public void test() {
        int a = IpV4Util.toInt("192.168.100.100");
        System.out.println(a);
        //a=a+200;
        System.out.println(IpV4Util.toIpAddress(a));
    }


    @Test
    public void test2() {
        reFresh("192.168.100.123", 2);
        DB.getIpTable().forEach(System.out::println);
    }

    @Test
    public void test3() throws IOException {
        System.out.println(IpV4Util.ping("www.baidu.com"));
    }
}
