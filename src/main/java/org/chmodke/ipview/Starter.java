package org.chmodke.ipview;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.chmodke.ipview.buis.ip.job.JobListener;
import org.chmodke.ipview.common.core.DispatcherServlet;
import org.chmodke.ipview.common.core.config.GlobalConfig;
import org.chmodke.logo.Logo;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ErrorPageErrorHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

import javax.servlet.ServletException;

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
        Logo.print();

        int port = GlobalConfig.getInteger("server.port", 8090);
        logger.info(String.format("Starter.main,serverPort is:%s", port));

        try {
            Server server = new Server(port);

            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            context.setResourceBase(Starter.class.getClassLoader().getResource("META-INF/resources").toURI().toString());

            //DefaultServlet
            String staticSuffix = GlobalConfig.getProperties("static.suffix", "*.html|*.js|*.css");
            for (String suffix : staticSuffix.split("\\|")) {
                if (StringUtils.isBlank(suffix)) {
                    continue;
                }
                logger.info(String.format("DefaultServlet apply to %s", suffix));
                context.addServlet(DefaultServlet.class, suffix);
            }

            //DispatcherServlet
            context.addServlet(DispatcherServlet.class, "/");
            context.addEventListener(new JobListener());

            server.setHandler(context);

            ErrorPageErrorHandler error = new ErrorPageErrorHandler();
            error.addErrorPage(ServletException.class, "/error");
            context.setErrorHandler(error);

            ServerConnector connector = server.getBean(ServerConnector.class);
            connector.setIdleTimeout(GlobalConfig.getInteger("server.timeout", 90000));

            server.start();
            if (logger.isDebugEnabled() && GlobalConfig.getBoolean("dumpStdErr"))
                server.dumpStdErr();
            server.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
