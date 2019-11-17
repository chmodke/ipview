package org.chmodke.ipview.buis.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.chmodke.ipview.common.core.anno.Controller;
import org.chmodke.ipview.common.core.anno.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;

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

    @RequestMapping("/getJsonStr")
    public String getJsonStr(HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(Arrays.asList(1, 2, 3));
        return json;
    }

    @RequestMapping("/getJson")
    public List getJson(HttpServletRequest request, HttpServletResponse response) {
        return Arrays.asList(1, 2, 3);
    }

    @RequestMapping("/getPerson")
    public Person getPerson(HttpServletRequest request, HttpServletResponse response) {
        return new Person("qwe", "23");
    }
}

class Person {
    private String name;
    private String age;

    public Person(String name, String age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
