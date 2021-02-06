package org.chmodke.ipview.buis.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.FullHttpRequest;
import org.apache.commons.lang3.StringUtils;
import org.chmodke.ipview.buis.ip.DB;
import org.chmodke.mvc.core.anno.Controller;
import org.chmodke.mvc.core.anno.RequestMapping;
import org.chmodke.mvc.core.anno.RespJson;
import org.chmodke.mvc.netty.RequestParser;

import java.io.IOException;
import java.util.Map;

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
    @RespJson
    public String getIpList(FullHttpRequest request) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> paramMap = RequestParser.parse(request);
        String qryIp = paramMap.get("IP");
        String json = "{}";
        if (StringUtils.isNotBlank(qryIp)) {
            json = mapper.writeValueAsString(DB.getByIp(qryIp));
        } else {
            json = mapper.writeValueAsString(DB.getIpTable());
        }
        return json;
    }
}
