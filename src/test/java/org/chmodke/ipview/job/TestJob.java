package org.chmodke.ipview.job;

import org.chmodke.ipview.buis.ip.DB;
import org.chmodke.ipview.buis.ip.job.RefreshIpJob;
import org.junit.Test;

/****************************************************************  
 * <p>Filename:    TestJob.java 
 * <p>Description:	 
 * <p>
 * <p>Create at:   2019/11/13 22:40
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

public class TestJob {
    @Test
    public void test2() {
        RefreshIpJob.getInstance().init();
        RefreshIpJob.getInstance().reFresh("192.168.100.123", 2);
        DB.getIpTable().forEach(System.out::println);
    }

}
