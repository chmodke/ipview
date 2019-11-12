package org.chmodke.ipview.buis.data;

import java.util.LinkedHashMap;
import java.util.Map;

/****************************************************************  
 * <p>Filename:    LRUHashMap.java 
 * <p>Description:	 
 * <p>
 * <p>Create at:   2019/11/12 13:03
 * <p> 
 * <p>Modification History:  
 * <p>Date          Author  Version   Description  
 * <p>------------------------------------------------------------------  
 * <p>2019/11/12    kehao     1.0        
 * <p>------------------------------------------------------------------
 *
 * @author kehao
 * @version 1.0
 * @since
 *******************************************************************/

public class LRUHashMap<K, V> extends LinkedHashMap<K, V> {
    private static final long serialVersionUID = 1L;

    private int maxSize;

    public LRUHashMap(int maxSize) {
        super(16, 0.75f, true);
        this.maxSize = maxSize;
    }

    public int getMaxSize() {
        return maxSize;
    }

    @Override
    protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
        return size() > maxSize;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }
}
