package org.chmodke.ipview.common.core.resolver;

import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.chmodke.ipview.common.core.entity.ModelAndView;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.net.URI;

/****************************************************************  
 * <p>Filename:    FreemarkerViewResolver.java 
 * <p>Description:	 freemarker模板处理引擎
 * <p>
 * <p>Create at:   2019/10/20 23:28
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

public class FreemarkerViewResolver {
    private Configuration cfg = null;
    private String defaultEncoding = "UTF-8";

    public FreemarkerViewResolver() {

    }

    public FreemarkerViewResolver(String defaultEncoding) {
        this.defaultEncoding = defaultEncoding;
    }

    /**
     * 初始化freemarker引擎
     *
     * @param viewDirectory
     * @throws IOException
     * @author kehao
     * @email kehao@asiainfo.com
     * @date 2019/10/20 23:41
     */
    public void init(URI viewDirectory) throws IOException {
        cfg = new Configuration();
        cfg.setDefaultEncoding(defaultEncoding);
        cfg.setDirectoryForTemplateLoading(new File(viewDirectory));
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    /**
     * 视图解析方法
     *
     * @param mav
     * @param response
     * @throws ParseException
     * @throws IOException
     * @throws TemplateException
     * @author kehao
     * @email kehao@asiainfo.com
     * @date 2019/10/20 23:41
     */
    public void resolver(ModelAndView mav, HttpServletResponse response) throws ParseException, IOException, TemplateException {
        Template template = cfg.getTemplate(mav.getViewName());
        Writer out = response.getWriter();
        template.process(mav.getRoot(), out);
        out.flush();
    }
}
