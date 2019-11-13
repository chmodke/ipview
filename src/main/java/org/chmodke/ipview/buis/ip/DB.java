package org.chmodke.ipview.buis.ip;

import org.chmodke.ipview.buis.exception.KeyIsNullException;
import org.chmodke.ipview.buis.ip.utils.IpV4Util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;

/****************************************************************  
 * <p>Filename:    DB.java 
 * <p>Description:	 
 * <p>
 * <p>Create at:   2019/11/8 22:04
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

public final class DB {
    public static final String IP_ADDRESS = "IP";
    private static ArrayList<HashMap<String, String>> ipTable = new ArrayList<HashMap<String, String>>();

    public static ArrayList<HashMap<String, String>> getIpTable() {
        ipTable.sort(new Comparator<HashMap<String, String>>() {
            @Override
            public int compare(HashMap<String, String> o1, HashMap<String, String> o2) {
                return IpV4Util.toInt(o1.get(IP_ADDRESS)) - IpV4Util.toInt(o2.get(IP_ADDRESS));
            }
        });
        return ipTable;
    }

    public static void updateIpTable(HashMap<String, String> table) {
        if (!table.containsKey(IP_ADDRESS)) {
            throw new KeyIsNullException(IP_ADDRESS);
        }
        synchronized (ipTable) {
            STATUS.setDbStatus(STATUS.DB_STATUS_UPD);
            boolean contains = false;
            Iterator<HashMap<String, String>> it = ipTable.iterator();
            while (it.hasNext()) {
                String ip = table.get(IP_ADDRESS);
                if (ip != null && ip.equals(it.next().get(IP_ADDRESS))) {
                    it.remove();
                    ipTable.add(table);
                    contains = true;
                    break;
                }
            }
            if (!contains) {
                ipTable.add(table);
            }
            STATUS.setDbStatus(STATUS.DB_STATUS_OK);
        }
    }

    public static void updateIpTable(ArrayList<HashMap<String, String>> newTable) {
        synchronized (ipTable) {
            ipTable = newTable;
            STATUS.setDbStatus(STATUS.DB_STATUS_OK);
        }
    }

    public static void clearIpTable() {
        synchronized (ipTable) {
            STATUS.setDbStatus(STATUS.DB_STATUS_CLS);
            ipTable.clear();
        }
    }
}
