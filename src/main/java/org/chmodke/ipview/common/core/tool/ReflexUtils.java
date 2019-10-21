package org.chmodke.ipview.common.core.tool;

import org.chmodke.ipview.common.core.anno.Controller;
import org.chmodke.ipview.common.core.anno.RequestMapping;
import org.chmodke.ipview.common.core.entity.URIInfo;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/****************************************************************  
 * <p>Filename:    ReflexUtils.java 
 * <p>Description:	 反射相关工具类
 * 根据注解把控制器添加到容器，根据注解存储映射的具体信息（映射路径，控制器方法）
 * <p>
 * <p>Create at:   2019/10/20 23:24
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

public class ReflexUtils {
    /**
     * 根据包名反射解析所有的控制器
     *
     * @param packageName
     * @return
     * @throws URISyntaxException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @author kehao
     * @email kehao@asiainfo.com
     * @date 2019/10/20 23:26
     */
    public static Map<String, Object> analysisController(String packageName) throws URISyntaxException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Map<String, Object> controllersMap = new HashMap<String, Object>();
        ClassLoader classLoader = ReflexUtils.class.getClassLoader();
        String packageDirectory = packageName.replace(".", "/");
        File directory = new File(classLoader.getResource(packageDirectory).toURI());
        for (File file : directory.listFiles()) {
            String fName = file.getName();
            if (!fName.endsWith(".class")) {
                continue;
            }
            fName = fName.substring(0, fName.length() - 6);
            String className = packageName + "." + fName;
            Class cl = classLoader.loadClass(className);
            //检查解析包中的类是否带有Controller注解，没有注解不往容器中添加实例
            Annotation controllerAnnotation = cl.getAnnotation(Controller.class);
            if (controllerAnnotation != null) {
                controllersMap.put(UUID.randomUUID().toString(), cl.newInstance());
            }
        }
        return controllersMap;
    }

    /**
     * 解析控制器上和方法上的RequestMapping注解，并存储对应的方法到容器
     *
     * @param controllersMap
     * @return
     * @author kehao
     * @email kehao@asiainfo.com
     * @date 2019/10/20 23:26
     */
    public static Map<String, URIInfo> analysisURIByController(Map<String, Object> controllersMap) {
        Map<String, URIInfo> uriInfosMap = new HashMap<String, URIInfo>();
        for (String controllerId : controllersMap.keySet()) {
            Object controller = controllersMap.get(controllerId);
            Class controllerClass = controller.getClass();
            String uriPrefix = "";
            //解析类controller上的ReqestMapping注解
            Annotation cRequestMappingAnnotation = controllerClass.getAnnotation(RequestMapping.class);
            if (cRequestMappingAnnotation != null) {
                RequestMapping requestMapping = (RequestMapping) cRequestMappingAnnotation;
                uriPrefix = requestMapping.value();
            }
            Method[] controllerMethods = controllerClass.getMethods();
            for (Method controllerMethod : controllerMethods) {
                Annotation mRequestMappingAnnotation = controllerMethod.getAnnotation(RequestMapping.class);
                if (mRequestMappingAnnotation != null) {
                    RequestMapping requestMapping = (RequestMapping) mRequestMappingAnnotation;
                    String uri = uriPrefix + requestMapping.value();
                    URIInfo uriInfo = new URIInfo(controllerId, controllerMethod);
                    uriInfosMap.put(uri, uriInfo);
                }
            }
        }
        return uriInfosMap;
    }
}
