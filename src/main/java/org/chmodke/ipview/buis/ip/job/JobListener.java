package org.chmodke.ipview.buis.ip.job;

import org.chmodke.ipview.common.config.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Date;
import java.util.Timer;

/****************************************************************  
 * <p>Filename:    JobListener.java 
 * <p>Description:	 
 * <p>
 * <p>Create at:   2019/11/10 16:17
 * <p> 
 * <p>Modification History:  
 * <p>Date          Author  Version   Description  
 * <p>------------------------------------------------------------------  
 * <p>2019/11/10    kehao     1.0        
 * <p>------------------------------------------------------------------
 *
 * @author kehao
 * @version 1.0
 * @since
 *******************************************************************/

public class JobListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(JobListener.class);
    private static final int DEFAULT_TIME_INTERVAL = 5 * 60;//秒
    private static int waitTime = 0;//秒

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        waitTime = AppConfig.getInteger("timeInterval", 5) * 60;
        if (waitTime <= 0) {
            waitTime = DEFAULT_TIME_INTERVAL;
        }
        logger.info("JobListener.contextInitialized,timeInterval is:{}ms", waitTime);

        Timer timer = new Timer();
        RefreshIpJob job = RefreshIpJob.getInstance();
        job.init();
        timer.schedule(job, new Date(), waitTime * 1000);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    public static int getWaitTime() {
        return waitTime;
    }
}
