package org.chmodke.ipview.buis.ip.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chmodke.ipview.buis.BuisConst;
import org.chmodke.ipview.buis.ip.DB;
import org.chmodke.ipview.buis.ip.utils.IpV4Util;
import org.chmodke.ipview.common.core.config.GlobalConfig;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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
    private ThreadPoolExecutor executor = null;

    public void init() {
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        int poolSize = Runtime.getRuntime().availableProcessors() * 2;
        BlockingQueue queue = new LinkedBlockingQueue(GlobalConfig.getInteger("scanLength"));
        executor = new ThreadPoolExecutor(corePoolSize, poolSize, 1, TimeUnit.SECONDS, queue);
        executor.allowCoreThreadTimeOut(true);
    }

    @Override
    public void run() {
        try {
            logger.info("RefreshIpJob execut date " + BuisConst.formatter.format(Calendar.getInstance().getTime()));
            reFresh(GlobalConfig.getProperties("startIp"), GlobalConfig.getInteger("scanLength"));
        } catch (Exception e) {
            logger.error("RefreshIpJob.run->Exception:", e);
        }
    }


    public void reFresh(String startIp, int length) {
        int start = IpV4Util.toInt(startIp);

        for (int i = 0; i <= length; i++) {
            String ipAddress = IpV4Util.toIpAddress(start + i);
            Scan scan = new Scan(ipAddress, GlobalConfig.getInteger("pingTimeOut", 1000));
            executor.execute(scan);
        }
    }

    public void reFresh(String startIp, String endIp) {
        int start = IpV4Util.toInt(startIp);
        int end = IpV4Util.toInt(endIp);
        int length = end - start;

        reFresh(startIp, length);
    }


    @Test
    public void test() {
        int a = IpV4Util.toInt("10.135.125.148");
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
        System.out.println(IpV4Util.ping("10.135.125.94", 500));
    }

    class Scan implements Runnable {
        private String ipAddress;
        private int timeout;

        Scan(String ipAddress) {
            this.ipAddress = ipAddress;
            this.timeout = 1000;
        }

        Scan(String ipAddress, int timeout) {
            this.ipAddress = ipAddress;
            this.timeout = timeout;
        }

        @Override
        public void run() {
            HashMap<String, String> ip = new HashMap<String, String>();
            ip.put(DB.IP_ADDRESS, ipAddress);
            ip.put("LAST_UP_TIME", BuisConst.formatter.format(Calendar.getInstance().getTime()));
            if (IpV4Util.ping(ipAddress, timeout)) {
                ip.put("STATUS", BuisConst.STATUS_ALIVE);
                ip.put("HOSTNAME", IpV4Util.getHostName(ip.get(DB.IP_ADDRESS)));
                if (logger.isDebugEnabled())
                    logger.debug(String.format("Address:%s is:%s", ipAddress, "Alive"));
            } else {
                ip.put("STATUS", BuisConst.STATUS_DEAD);
                ip.put("HOSTNAME", "N/A");
                if (logger.isDebugEnabled())
                    logger.debug(String.format("Address:%s is:%s", ipAddress, "Dead"));
            }
            DB.updateIpTable(ip);
        }
    }
}
