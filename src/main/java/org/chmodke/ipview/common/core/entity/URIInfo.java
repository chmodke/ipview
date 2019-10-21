package org.chmodke.ipview.common.core.entity;

import java.lang.reflect.Method;

/****************************************************************  
 * <p>Filename:    URIInfo.java 
 * <p>Description:	 uri的实体类，包含uri对应的控制器和控制器方法
 * <p>
 * <p>Create at:   2019/10/20 23:23
 * <p> 
 * <p>Modification History:  
 * <p>Date          Author  Version   Description  
 * <p>------------------------------------------------------------------  
 * <p>2019/10/20    kehao     1.0        
 * <p>------------------------------------------------------------------
 *
 * @author kehao
 * @version 1.0
 * @since
 *******************************************************************/

public class URIInfo {
    //控制器键
    private String controllerId;
    //控制器方法
    private Method uriMethod;

    public URIInfo() {
    }

    public URIInfo(String controllerId, Method uriMethod) {
        this.controllerId = controllerId;
        this.uriMethod = uriMethod;
    }

    public String getControllerId() {
        return controllerId;
    }

    public void setControllerId(String controllerId) {
        this.controllerId = controllerId;
    }

    public Method getUriMethod() {
        return uriMethod;
    }

    public void setUriMethod(Method uriMethod) {
        this.uriMethod = uriMethod;
    }

}
