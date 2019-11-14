package org.chmodke.ipview.buis.ip.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chmodke.ipview.buis.BuisConst;
import org.chmodke.ipview.buis.ip.DB;
import org.chmodke.ipview.buis.ip.utils.IpV4Util;
import org.chmodke.ipview.common.core.config.GlobalConfig;

import java.util.Calendar;
import java.util.HashMap;
import java.util.TimerTask;
import java.util.concurrent.*;

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
    private static RefreshIpJob job = new RefreshIpJob();
    private ThreadPoolExecutor executor = null;

    private RefreshIpJob() {
    }

    public static RefreshIpJob getInstance() {
        job.init();
        return job;
    }

    public void init() {
        int coreSize = Runtime.getRuntime().availableProcessors();
        int threadLength = GlobalConfig.getInteger("scanLength");

        //int poolSize = (int) Math.floor((0.5 + 1.5) / 0.1 * coreSize);
        int poolSize = (int) Math.floor(threadLength / coreSize);
        int corePoolSize = poolSize / 2;

        int t = threadLength - corePoolSize - (poolSize - corePoolSize);
        BlockingQueue queue = new LinkedBlockingQueue(threadLength - poolSize);
        RejectedExecutionHandler handler = new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                logger.error(String.format("线程溢出 %s", r.toString()));
            }
        };
        //corePoolSize 核心线程数量
        //poolSize 线程池最大容量
        //queue 核心线程阻塞队列，只服务于核心线程
        //当提交的线程数量总和 - 已执行过的线程 - 核心线程数量 - 阻塞队列长度 > 线程池最大容量时会发生队列溢出
        executor = new ThreadPoolExecutor(corePoolSize, poolSize, 0, TimeUnit.SECONDS, queue, handler);
        //executor.allowCoreThreadTimeOut(true);
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
            if (IpV4Util.pingCmd(ipAddress, timeout)) {
                ip.put("STATUS", BuisConst.STATUS_ALIVE);
                ip.put("HOSTNAME", IpV4Util.getHostName(ip.get(DB.IP_ADDRESS)));
            } else {
                ip.put("STATUS", BuisConst.STATUS_DEAD);
                ip.put("HOSTNAME", "N/A");
            }
            DB.updateIpTable(ip);
        }

        @Override
        public String toString() {
            return "Scan" + this.ipAddress;
        }
    }
}
