package org.chmodke.ipview.buis.web;

import io.netty.handler.codec.http.FullHttpRequest;
import org.chmodke.ipview.buis.ip.DB;
import org.chmodke.ipview.buis.ip.STATUS;
import org.chmodke.ipview.buis.ip.job.JobListener;
import org.chmodke.ipview.buis.ip.job.RefreshIpJob;
import org.chmodke.mvc.core.anno.Controller;
import org.chmodke.mvc.core.anno.RequestMapping;
import org.chmodke.mvc.core.entity.ModelAndView;
import org.chmodke.mvc.netty.RequestParser;
import org.chmodke.mvc.netty.exception.MethodNotSupportedException;

import java.io.*;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

/****************************************************************  
 * <p>Filename:    WebController.java 
 * <p>Description:	 
 * <p>
 * <p>Create at:   2019/10/21 0:16
 * <p> 
 * <p>Modification History:  
 * <p>Date          Author  Version   Description  
 * <p>------------------------------------------------------------------  
 * <p>2019/10/21    kehao     1.0        
 * <p>------------------------------------------------------------------
 *
 * @author kehao
 * @version 1.0
 * @since
 *******************************************************************/

@Controller
public class WebController {
    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @RequestMapping("/index")
    public ModelAndView index(FullHttpRequest request) throws IOException {
        ModelAndView mav = new ModelAndView("index.ftl");
        mav.addObject("name", "欢迎访问!");
        return mav;
    }

    @RequestMapping("/")
    public ModelAndView root(FullHttpRequest request) throws IOException {
        return list(request);
    }

    @RequestMapping("/refresh")
    public ModelAndView refresh(FullHttpRequest request) throws IOException {
        ModelAndView mav = new ModelAndView("refresh.ftl");
        if (STATUS.DB_STATUS_OK == STATUS.getDbStatus()) {
            RefreshIpJob.getInstance().run();
        }
        return mav;
    }


    @RequestMapping("/list")
    public ModelAndView list(FullHttpRequest request) throws IOException {
        ModelAndView mav = null;
        if (STATUS.DB_STATUS_OK == STATUS.getDbStatus()) {
            mav = new ModelAndView("list.ftl");
            mav.addObject("ip_list", DB.getIpTable());
            mav.addObject("timeInterval", JobListener.getWaitTime());
            mav.addObject("nowDate", dtf.format(LocalDateTime.now()));
        } else {
            mav = new ModelAndView("refresh.ftl");
        }
        return mav;
    }

    @RequestMapping("/getmd")
    public String getmd(FullHttpRequest request) throws IOException, MethodNotSupportedException {
        Map<String, String> paramMap = RequestParser.parse(request);
        String mdid = paramMap.get("mdid");
        StringBuffer mdContext = new StringBuffer();
        URL mdPath = WebController.class.getClassLoader().getResource("META-INF/resources/md/" + mdid);
        File mdFile = new File(mdPath.getPath());
        BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(mdFile), "UTF-8"));
        int len = 0;
        String line = null;
        try {
            while ((line = in.readLine()) != null) {
                if (len != 0) {// 处理换行符的问题
                    mdContext.append("\r\n" + line);
                } else {
                    mdContext.append(line);
                }
                len++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            in.close();
        }
        return mdContext.toString();
    }

    @RequestMapping("/error")
    public ModelAndView error(FullHttpRequest request) throws IOException {
        ModelAndView mav = new ModelAndView("error.ftl");
        mav.addObject("name", "欢迎进入首页!");
        return mav;
    }

}
