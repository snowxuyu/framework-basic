package org.framework.basic.server.netty.initializer;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * 功能描述：<code>ChannelInitializerWrapper</code>是Channel Handler与业务容器的包装类。</p>
 *
 */
public abstract class ChannelInitializerWrapper extends ChannelInitializer<SocketChannel> {
  
    public abstract void shutdown();
}
