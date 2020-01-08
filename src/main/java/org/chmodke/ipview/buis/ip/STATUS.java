package org.chmodke.ipview.buis.ip;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/****************************************************************  
 * <p>Filename:    STATUS.java 
 * <p>Description:	 
 * <p>
 * <p>Create at:   2019/11/10 16:33
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

public class STATUS {
    private static final Logger logger = LoggerFactory.getLogger(STATUS.class);
    public static int DB_STATUS_INIT = 0;
    public static int DB_STATUS_CLS = 1;
    public static int DB_STATUS_UPD = 2;
    public static int DB_STATUS_OK = 9;


    private static int dbStatus = DB_STATUS_INIT;

    public static int getDbStatus() {
        return dbStatus;
    }

    public synchronized static void setDbStatus(int dbStatus) {
        STATUS.dbStatus = dbStatus;
        if (logger.isDebugEnabled())
            logger.debug("STATUS.setDbStatus->dbStatus:{}", dbStatus);
    }
}
