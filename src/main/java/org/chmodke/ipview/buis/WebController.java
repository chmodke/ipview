package org.chmodke.ipview.buis;

import org.chmodke.ipview.buis.ip.DB;
import org.chmodke.ipview.buis.ip.STATUS;
import org.chmodke.ipview.buis.ip.job.RefreshIpJob;
import org.chmodke.ipview.common.core.anno.Controller;
import org.chmodke.ipview.common.core.anno.RequestMapping;
import org.chmodke.ipview.common.core.entity.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/****************************************************************  
 * <p>Filename:    TestController.java 
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

    @RequestMapping("/index")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView mav = new ModelAndView("index.ftl");
        mav.addObject("name", "欢迎访问!");
        return mav;
    }

    @RequestMapping("/")
    public ModelAndView root(HttpServletRequest request, HttpServletResponse response) throws IOException {
        return list(request, response);
    }

    @RequestMapping("/refresh")
    public ModelAndView refresh(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView mav = new ModelAndView("refresh.ftl");
        if (STATUS.DB_STATUS_OK == STATUS.getDbStatus()) {
            new RefreshIpJob().run();
        }
        return mav;
    }


    @RequestMapping("/list")
    public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView mav = null;
        if (STATUS.DB_STATUS_OK == STATUS.getDbStatus()) {
            mav = new ModelAndView("list.ftl");
            mav.addObject("ip_list", DB.getIpTable());
        } else {
            mav = new ModelAndView("refresh.ftl");
        }
        return mav;
    }

    @RequestMapping("/error")
    public ModelAndView error(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView mav = new ModelAndView("error.ftl");
        mav.addObject("name", "欢迎进入首页!");
        return mav;
    }

}
