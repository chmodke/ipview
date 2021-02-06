package org.chmodke.ipview;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.apache.commons.lang3.StringUtils;
import org.chmodke.ipview.buis.ip.job.JobListener;
import org.chmodke.ipview.http.HttpServerInitializer;
import org.chmodke.logo.Logo;
import org.chmodke.mvc.core.config.MvcConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

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
    private static final Logger logger = LoggerFactory.getLogger(Starter.class);
    private static Thread deamon;

    public static void main(String[] args) {
        Logo.print();

        int port = MvcConfig.getInteger("server.port", 8090);
        String serverPort = System.getProperty("server.port");
        if (StringUtils.isNotBlank(serverPort)) {
            try {
                port = Integer.parseInt(serverPort);
            } catch (NumberFormatException e) {
                logger.warn("Starter.main,VM Option [server.port={}] illegal.", serverPort);
            }
        }
        logger.info("Starter.main,serverPort use:{}.", port);

        try {
            ServerBootstrap bootstrap = new ServerBootstrap();
            EventLoopGroup boss = new NioEventLoopGroup();
            EventLoopGroup work = new NioEventLoopGroup();
            bootstrap.group(boss, work)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new HttpServerInitializer());
            ChannelFuture f = bootstrap.bind(new InetSocketAddress(port)).sync();
            logger.info("Starter.main,finshed http://{}:{}{}.", "127.0.0.1", port,
                    MvcConfig.getProperties("server.context.path", "/"));
            initialize();
            f.channel().closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void initialize() {
        deamon = new Thread(new Runnable() {

            @Override
            public void run() {
                JobListener job = new JobListener();
                job.contextInitialized();
                logger.info("Starter.initialize,ScanJob was notified");
                deamon.interrupt();
            }
        }, "Start_Deamon");
        deamon.start();
    }
}
