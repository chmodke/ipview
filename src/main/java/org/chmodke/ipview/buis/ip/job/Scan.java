package org.chmodke.ipview.buis.ip.job;

import org.chmodke.ipview.buis.BuisConst;
import org.chmodke.ipview.buis.ip.DB;
import org.chmodke.ipview.buis.ip.utils.IpV4Util;

import java.text.DateFormat;
import java.util.HashMap;

/****************************************************************  
 * <p>Filename:    Scan.java 
 * <p>Description:	 
 * <p>
 * <p>Create at:   2019/11/15 23:33
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

public class Scan implements Runnable {
    private String ipAddress;
    private int timeout;

    public Scan(String ipAddress) {
        this.ipAddress = ipAddress;
        this.timeout = 1000;
    }

    public Scan(String ipAddress, int timeout) {
        this.ipAddress = ipAddress;
        this.timeout = timeout;
    }

    @Override
    public void run() {
        HashMap<String, String> ip = new HashMap<String, String>();
        ip.put(DB.IP_ADDRESS, ipAddress);
        ip.put("LAST_UP_TIME", DateFormat.getDateTimeInstance().format(System.currentTimeMillis()));
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
