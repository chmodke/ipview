package org.chmodke.ipview.buis.ip.job;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/****************************************************************  
 * <p>Filename:    PingThreadFactory.java 
 * <p>Description:	 
 * <p>
 * <p>Create at:   2020/1/8 21:56
 * <p> 
 * <p>Modification History:  
 * <p>Date          Author  Version   Description  
 * <p>------------------------------------------------------------------  
 * <p>2020/1/8    kehao     1.0        
 * <p>------------------------------------------------------------------
 *
 * @author kehao
 * @version 1.0
 * @since
 *******************************************************************/

public class PingThreadFactory implements ThreadFactory {
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(r);
        t.setName("pool-ping-" + atomicInteger.incrementAndGet());
        if (atomicInteger.get() == Integer.MAX_VALUE) {
            atomicInteger.set(0);
        }
        return t;
    }
}
