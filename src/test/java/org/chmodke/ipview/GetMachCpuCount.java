package org.chmodke.ipview;

import org.junit.Test;

/****************************************************************  
 * <p>Filename:    GetMachCpuCount.java 
 * <p>Description:	 
 * <p>
 * <p>Create at:   2019/11/13 22:37
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

public class GetMachCpuCount {
    @Test
    public void test() {
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        System.out.println(corePoolSize);
    }
}
