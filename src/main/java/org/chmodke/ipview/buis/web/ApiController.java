package org.chmodke.ipview.buis.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.chmodke.ipview.buis.ip.DB;
import org.chmodke.ipview.common.core.anno.Controller;
import org.chmodke.ipview.common.core.anno.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/****************************************************************  
 * <p>Filename:    ApiController.java 
 * <p>Description:	 
 * <p>
 * <p>Create at:   2019/11/17 21:56
 * <p> 
 * <p>Modification History:  
 * <p>Date          Author  Version   Description  
 * <p>------------------------------------------------------------------  
 * <p>2019/11/17    kehao     1.0        
 * <p>------------------------------------------------------------------
 *
 * @author kehao
 * @version 1.0
 * @since
 *******************************************************************/

@Controller
@RequestMapping("/api")
public class ApiController {

    @RequestMapping("/getIpList")
    public String getIpList(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String qryIp = request.getParameter("IP");
        String json = "{}";
        if (StringUtils.isNotBlank(qryIp)) {
            json = mapper.writeValueAsString(DB.getByIp(qryIp));
        } else {
            json = mapper.writeValueAsString(DB.getIpTable());
        }
        return json;
    }
}
