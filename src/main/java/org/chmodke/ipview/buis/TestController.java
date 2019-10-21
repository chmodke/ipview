package org.chmodke.ipview.buis;

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
public class TestController {
    @RequestMapping("/")
    public ModelAndView index(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView mav = new ModelAndView("index.ftl");
        mav.addObject("name", "欢迎进入首页!");   
        return mav;
    }

    @RequestMapping("/error")
    public ModelAndView error(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView mav = new ModelAndView("error.ftl");
        mav.addObject("name", "欢迎进入首页!");
        return mav;
    }

}
