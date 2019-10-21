package org.chmodke.ipview;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chmodke.ipview.common.core.DispatcherServlet;
import org.chmodke.ipview.common.core.config.GlobalConfig;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ErrorPageErrorHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.ServletException;
import java.net.URISyntaxException;

/****************************************************************  
 * <p>Filename:    Start.java 
 * <p>Description:	 
 * <p>
 * <p>Create at:   2019/10/21 0:20
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

public class Starter {
    private static Log logger = LogFactory.getLog(Starter.class);

    public static void main(String[] args) {
        try {
            Server server = new Server(8090);

            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            context.setResourceBase(Starter.class.getClassLoader().getResource("META-INF/resources").toURI().toString());
            server.setHandler(context);

            context.addServlet(DispatcherServlet.class, "/");

            ErrorPageErrorHandler error = new ErrorPageErrorHandler();
            error.addErrorPage(ServletException.class, "/error");
            context.setErrorHandler(error);
            
            server.start();
            if (logger.isDebugEnabled() && Boolean.parseBoolean(GlobalConfig.getProperties("dumpStdErr")))
                server.dumpStdErr();
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
