package org.chmodke.ipview.common.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import freemarker.log.Logger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chmodke.ipview.common.core.config.GlobalConfig;
import org.chmodke.ipview.common.core.entity.ModelAndView;
import org.chmodke.ipview.common.core.entity.URIInfo;
import org.chmodke.ipview.common.core.resolver.FreemarkerViewResolver;
import org.chmodke.ipview.common.core.tool.ReflexUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/****************************************************************  
 * <p>Filename:    DispatcherServlet.java 
 * <p>Description:	 
 * <p>
 * <p>Create at:   2019/10/20 23:44
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

public class DispatcherServlet extends HttpServlet {
    private static final Log logger = LogFactory.getLog(DispatcherServlet.class);
    private static final long serialVersionUID = 1762792866529445537L;
    //定义存储所有的控制器的容器
    private Map<String, Object> controllersMap = null;
    //定义存储uri信息容器
    private Map<String, URIInfo> uriInfosMap = null;
    //创建模板引擎
    private FreemarkerViewResolver viewResolver = null;
    //编码默认使用utf-8
    private String defaultCharacterEncoding = "UTF-8";

    @Override
    public void init() throws ServletException {
        String mvcPackage = GlobalConfig.getProperties("mvcPackage");
        String viewDirectory = GlobalConfig.getProperties("viewDirectory");
        defaultCharacterEncoding = GlobalConfig.getProperties("characterEncoding", defaultCharacterEncoding);
        try {
            logger.info(String.format("scan controller package : %s", mvcPackage));
            controllersMap = ReflexUtils.analysisController(mvcPackage);
            if (logger.isInfoEnabled()) {
                Iterator<Map.Entry<String, Object>> it = controllersMap.entrySet().iterator();
                while (it.hasNext()) {
                    logger.info(String.format("scanted controller : %s", it.next().getValue().getClass().getSimpleName()));
                }
            }

            uriInfosMap = ReflexUtils.analysisURIByController(controllersMap);
            if (logger.isInfoEnabled()) {
                Iterator<Map.Entry<String, URIInfo>> it = uriInfosMap.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, URIInfo> t = it.next();
                    logger.info(String.format("scanted url  : %s # %s", t.getKey(), t.getValue().getUriMethod().getName()));
                }
            }
            System.setProperty(Logger.SYSTEM_PROPERTY_NAME_LOGGER_LIBRARY, Logger.LIBRARY_NAME_NONE);
            viewResolver = new FreemarkerViewResolver(defaultCharacterEncoding);
            logger.info(String.format("CharacterEncoding : %s", defaultCharacterEncoding));
            viewResolver.init(getServletContext().getResource(viewDirectory).toURI());
        } catch (Exception e) {
            logger.error("DispatcherServlet.init->Exception:", e);
            throw new ServletException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding(defaultCharacterEncoding);
        resp.setCharacterEncoding(defaultCharacterEncoding);
        if (logger.isDebugEnabled())
            logger.debug(String.format("DispatcherServlet.url->:%s", req.getRequestURI() + "?" + req.getQueryString()));
        String uri = req.getRequestURI().replace(req.getServletContext().getContextPath(), "");
        URIInfo uriInfo = uriInfosMap.get(uri);

        if (null == uriInfo) {
            //没有正确的处理时的输出
            resp.getWriter().println("<body><h1>");
            resp.getWriter().println("404");
            resp.getWriter().println("</h1></body>");
            resp.getWriter().flush();
            return;
        }

        try {
            Object mavObject = uriInfo.getUriMethod().invoke(controllersMap.get(uriInfo.getControllerId()), req, resp);
            if (mavObject != null) {
                if (mavObject instanceof ModelAndView) {
                    resp.addHeader("Content-Type", "text/html;charset=utf-8");
                    viewResolver.resolver((ModelAndView) mavObject, resp);
                    return;
                } else if (mavObject instanceof String) {
                    resp.addHeader("Content-Type", "text/plain;charset=utf-8");
                    resp.getWriter().write((String) mavObject);
                    return;
                } else {
                    ObjectMapper mapper = new ObjectMapper();
                    String json = mapper.writeValueAsString(mavObject);
                    resp.getWriter().write(json);
                    resp.addHeader("Content-Type", "application/json;charset=utf-8");
                    return;
                }
            }
        } catch (Exception e) {
            //处理最外层捕捉错误，并抛到前台
            e.printStackTrace(resp.getWriter());
            return;
        }
        //没有正确的处理时的输出
        resp.getWriter().println("^_^ error!");
        resp.getWriter().flush();
    }
}
