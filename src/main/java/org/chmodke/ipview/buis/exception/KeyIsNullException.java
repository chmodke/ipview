package org.chmodke.ipview.buis.exception;

/****************************************************************  
 * <p>Filename:    KeyIsNullException.java 
 * <p>Description:	 
 * <p>
 * <p>Create at:   2019/11/8 22:18
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

public class KeyIsNullException extends RuntimeException {

    public KeyIsNullException(String key) {
        super(key);
    }
}
