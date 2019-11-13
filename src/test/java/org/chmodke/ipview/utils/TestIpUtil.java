package org.chmodke.ipview.utils;

import org.chmodke.ipview.buis.ip.utils.IpV4Util;
import org.junit.Test;

import java.io.IOException;

/****************************************************************  
 * <p>Filename:    TestIpUtil.java 
 * <p>Description:	 
 * <p>
 * <p>Create at:   2019/11/13 22:41
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

public class TestIpUtil {
    @Test
    public void test() {
        int a = IpV4Util.toInt("10.135.125.148");
        System.out.println(a);
        //a=a+200;
        System.out.println(IpV4Util.toIpAddress(a));
    }
    @Test
    public void test3() throws IOException {
        IpV4Util.ping("10.135.125.94", 500);
    }

    public void test4() {
        IpV4Util.pingCmd("192.168.100.96", 500);
        new Thread() {
            @Override
            public void run() {
                System.out.println(IpV4Util.getHostName("192.168.100.96"));
                System.out.println(IpV4Util.getCanonicalHostName("192.168.100.96"));
            }
        }.start();
        IpV4Util.pingCmd("192.168.100.100", 500);
        new Thread() {
            @Override
            public void run() {
                System.out.println(IpV4Util.getHostName("192.168.100.100"));
                System.out.println(IpV4Util.getCanonicalHostName("192.168.100.100"));
            }
        }.start();
    }
}
