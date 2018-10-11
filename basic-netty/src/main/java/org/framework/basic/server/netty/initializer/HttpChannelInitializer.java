package org.framework.basic.server.netty.initializer;

import io.netty.channel.ChannelPipeline;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.util.concurrent.DefaultEventExecutor;
import io.netty.util.concurrent.DefaultEventExecutorGroup;

import org.framework.basic.server.config.ServerConfig;
import org.framework.basic.server.netty.http.HttpServletHandler;
import org.framework.basic.server.servlet.ServletWebApp;
import org.framework.basic.server.servlet.config.WebAppConfiguration;
import org.framework.basic.server.servlet.interceptor.ChannelInterceptor;
import org.framework.basic.server.servlet.interceptor.HttpSessionInterceptor;
import org.framework.basic.server.servlet.session.DefaultHttpSessionStore;
import org.framework.basic.server.servlet.session.HttpSessionStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 功能描述： <code>HttpChannelInitializer</code>用于注册http请求相关的Handler。</p>
 */
public class HttpChannelInitializer extends ChannelInitializerWrapper {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private DefaultEventExecutor eventExecutor = new DefaultEventExecutor();

    private ChannelGroup channels = new DefaultChannelGroup(eventExecutor);

    private DefaultEventExecutorGroup businessExecutor;
    
    private HttpSessionWatchdog watchdog;

    public HttpChannelInitializer(WebAppConfiguration config) {
        
        // 初始化应用程序。
        ServletWebApp webapp = ServletWebApp.get();
        webapp.init(config, channels);

        businessExecutor = new DefaultEventExecutorGroup(ServerConfig.getBusinessThreadNumber());
        
        // 用户会话监控器。
        this.watchdog = new HttpSessionWatchdog();
        businessExecutor.execute(watchdog);
    }

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        // Configure SSL.
        if (ServerConfig.isSsl()) {
            SelfSignedCertificate ssc = new SelfSignedCertificate();
            SslContext sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
            //SslContext sslCtx = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
            if (sslCtx != null) {
                pipeline.addLast(sslCtx.newHandler(ch.alloc()));
            }
        }

        // Register HTTP handler chain.
        this.appendHttpPipeline(pipeline);
    }

    @Override
    public void shutdown() {
        this.watchdog.stopWatching();
        ServletWebApp.get().destroy();
        this.channels.close().awaitUninterruptibly();
    }

    public final ChannelPipeline appendHttpPipeline(ChannelPipeline channelPipeline) {
        channelPipeline.addLast("decoder", new HttpRequestDecoder());
        //channelPipeline.addLast("aggregator", new HttpObjectAggregator(1048576));
        channelPipeline.addLast("aggregator", new HttpObjectAggregator(ServerConfig.getMaxContentLength()));
        channelPipeline.addLast("encoder", new HttpResponseEncoder());

        // Remove the following line if you don't want automatic content compression.
        // channelPipeline.addLast("deflater", new HttpContentCompressor());
       
        HttpServletHandler servletHandler = new HttpServletHandler();
        servletHandler.addInterceptor(new ChannelInterceptor());
        servletHandler.addInterceptor(new HttpSessionInterceptor(getHttpSessionStore()));

        channelPipeline.addLast("handler", servletHandler);
        //channelPipeline.addLast(businessExecutor, new AsyncHttpServletHandler());

        return channelPipeline;
    }

    protected HttpSessionStore getHttpSessionStore() {
        return new DefaultHttpSessionStore();
    }

    private class HttpSessionWatchdog implements Runnable {

        private boolean shouldStopWatching = false;

        @Override
        public void run() {
            while (!shouldStopWatching) {

                try {
                    HttpSessionStore store = getHttpSessionStore();
                    if (store != null) {
                        store.destroyInactiveSessions();
                    }
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    return;
                }

            }

        }

        public void stopWatching() {
            this.shouldStopWatching = true;
        }
    }
}
