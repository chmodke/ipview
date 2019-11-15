package org.chmodke.ipview.buis.ip.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chmodke.ipview.common.core.config.GlobalConfig;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Calendar;
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
    private static final Log logger = LogFactory.getLog(JobListener.class);
    private static final int DEFAULT_TIME_INTERVAL = 5 * 60;//秒
    private static int waitTime = 0;//秒

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        waitTime = GlobalConfig.getInteger("timeInterval", 5) * 60;
        if (waitTime <= 0) {
            waitTime = DEFAULT_TIME_INTERVAL;
        }
        logger.info(String.format("JobListener.contextInitialized,timeInterval is:%sms", waitTime));

        Timer timer = new Timer();
        RefreshIpJob job = RefreshIpJob.getInstance();
        job.init();
        timer.schedule(job, Calendar.getInstance().getTime(), waitTime * 1000);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    public static int getWaitTime() {
        return waitTime;
    }
}
