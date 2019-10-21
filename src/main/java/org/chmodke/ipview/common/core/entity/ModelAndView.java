package org.chmodke.ipview.common.core.entity;

import java.util.HashMap;
import java.util.Map;

/****************************************************************  
 * <p>Filename:    ModelAndView.java 
 * <p>Description:	 包含视图和数据的实体类，主要用于freemarker渲染使用
 * <p>
 * <p>Create at:   2019/10/20 23:21
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

public class ModelAndView {
    //视图名称
    private String viewName = null;
    //模板根容器
    private Map root = null;

    public ModelAndView() {
        root = new HashMap();
    }

    public ModelAndView(String viewName) {
        this();
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map getRoot() {
        return root;
    }

    /**
     * 增加数据
     *
     * @param key
     * @param value
     * @author kehao
     * @email kehao@asiainfo.com
     * @date 2019/10/20 23:22
     */
    public void addObject(Object key, Object value) {
        root.put(key, value);
    }
}
