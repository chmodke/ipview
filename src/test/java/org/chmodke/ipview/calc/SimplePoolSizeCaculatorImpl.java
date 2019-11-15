package org.chmodke.ipview.calc;

import org.chmodke.ipview.buis.ip.job.Scan;

import java.lang.management.ManagementFactory;
import java.math.BigDecimal;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/****************************************************************  
 * <p>Filename:    SimplePoolSizeCaculatorImpl.java 
 * <p>Description:	 
 * <p>
 * <p>Create at:   2019/11/15 23:31
 * <p> 
 * <p>Modification History:  
 * <p>Date          Author  Version   Description  
 * <p>------------------------------------------------------------------  
 * <p>2019/11/15    kehao     1.0        
 * <p>------------------------------------------------------------------
 *
 * @author kehao
 * @version 1.0
 * @since
 *******************************************************************/

public class SimplePoolSizeCaculatorImpl extends PoolSizeCalculator {
    @Override
    protected Runnable creatTask() {
        return new Scan("192.168.100.101", 500);
    }

    @Override
    protected BlockingQueue createWorkQueue() {
        return new LinkedBlockingQueue(1000);
    }

    @Override
    protected long getCurrentThreadCPUTime() {
        return ManagementFactory.getThreadMXBean().getCurrentThreadCpuTime();
    }

    public static void main(String[] args) {
        PoolSizeCalculator poolSizeCalculator = new SimplePoolSizeCaculatorImpl();
        poolSizeCalculator.calculateBoundaries(new BigDecimal(1.0), new BigDecimal(100000));
    }

}