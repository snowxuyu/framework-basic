package org.framework.basic.server;

import org.framework.basic.server.annotation.NettyBootstrap;


public class NettyApplication {

    public static final void run(Class<?> clazz, String[] args) {
        NettyServerBootstrap nettyServerBootstrap = NettyServerBootstrap.createServer();
        if (null != clazz) {
            NettyBootstrap nb = clazz.getAnnotation(NettyBootstrap.class);
            String springApplicationContext = nb.applicationContext();
            String springServletContext = nb.servletContext();
            String serverProperties = nb.serverConfig();
            nettyServerBootstrap.setSpringApplicationContext(springApplicationContext)
                    .setSpringServletContext(springServletContext)
                    .setServerProperties(serverProperties);
        }
        nettyServerBootstrap.parseCommondArguments(args).start();

    }
}
