package org.chmodke.ipview.buis.ip.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chmodke.ipview.buis.ip.utils.IpV4Util;
import org.chmodke.ipview.common.config.AppConfig;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private RefreshIpJob() {
    }

    public static RefreshIpJob getInstance() {
        return job;
    }

    public void init() {
        int coreSisz = Runtime.getRuntime().availableProcessors();
        int threadLength = AppConfig.getInteger("scanLength");
        int corePoolSize = threadLength <= coreSisz * 10 ? threadLength : coreSisz * 10;
        int queueSize = (threadLength - corePoolSize) * 5 + threadLength;
        logger.info(String.format("corePoolSize=%s,poolSize=%s,queueSize=%s", corePoolSize, corePoolSize * 2, queueSize));
        BlockingQueue queue = new LinkedBlockingQueue(queueSize);
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

        //由于ping扫描的io开销很大，导致cpu利用率极低，开启多线程来提高效率
        executor = new ThreadPoolExecutor(corePoolSize, corePoolSize * 2, 0, TimeUnit.SECONDS, queue, handler);
        //executor.allowCoreThreadTimeOut(true);
    }

    @Override
    public void run() {
        try {
            logger.info("RefreshIpJob execut date " + dtf.format(LocalDateTime.now()));
            reFresh(AppConfig.getProperties("startIp"), AppConfig.getInteger("scanLength"));
        } catch (Exception e) {
            logger.error("RefreshIpJob.run->Exception:", e);
        }
    }


    public void reFresh(String startIp, int length) {
        int start = IpV4Util.toInt(startIp);

        for (int i = 0; i <= length; i++) {
            String ipAddress = IpV4Util.toIpAddress(start + i);
            Scan scan = new Scan(ipAddress, AppConfig.getInteger("pingTimeOut", 1000));
            executor.execute(scan);
        }
    }

    public void reFresh(String startIp, String endIp) {
        int start = IpV4Util.toInt(startIp);
        int end = IpV4Util.toInt(endIp);
        int length = end - start;

        reFresh(startIp, length);
    }
}
