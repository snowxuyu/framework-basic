package org.framework.basic.server.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.TooLongFrameException;
import io.netty.handler.codec.http.*;
import io.netty.handler.ssl.SslHandler;
import io.netty.handler.stream.ChunkedFile;
import org.framework.basic.server.config.ServerConfig;
import org.framework.basic.server.servlet.ServletWebApp;
import org.framework.basic.server.servlet.exception.NettyServletRuntimeException;
import org.framework.basic.server.servlet.impl.FilterChainImpl;
import org.framework.basic.server.servlet.impl.HttpServletRequestImpl;
import org.framework.basic.server.servlet.impl.HttpServletResponseImpl;
import org.framework.basic.server.servlet.interceptor.HttpServletInterceptor;
import org.framework.basic.server.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;


/**
 * 功能描述：<code>HttpServletHandler</code>是基于Netty {@link io.netty.channel.ChannelHandler} <b>的请求处理器.</p>
 * 它接收并解析HTTP请求，然后传递给Spring MVC请求控制器进行业务分发，最终响应结果到请求客户端。
 * Note:它不处理异步Servlet请求，对于异步请求仅仅封装为Job任务，由{@link AsyncHttpServletHandler}进行异步处理。
 *
 */
@ChannelHandler.Sharable
public class HttpServletHandler extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(HttpServletHandler.class);

    private List<HttpServletInterceptor> interceptors;

    /**
     * Which uri should be passed into this servlet container
     */
    private String uriPrefix = "/";

    public HttpServletHandler() {
        this("/");
    }

    public HttpServletHandler(String uriPrefix) {
        this.uriPrefix = uriPrefix;
    }

    public HttpServletHandler addInterceptor(HttpServletInterceptor interceptor) {
        if (interceptors == null) {
            interceptors = new ArrayList<HttpServletInterceptor>();
        }

        interceptors.add(interceptor);
        return this;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.debug("Opening new channel: {}", ctx.channel().id());
        ServletWebApp.get().getSharedChannelGroup().add(ctx.channel());

        ctx.fireChannelActive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object e) throws Exception {

        if (e instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) e;
            String uri = request.uri();
            log.debug("The current request uri:" + uri);
            if (uri.startsWith(uriPrefix)) {
/*                if (HttpHeaders.is100ContinueExpected(request)) {
                    ctx.channel().write(new DefaultHttpResponse(HTTP_1_1, CONTINUE));
                }*/

                FilterChainImpl chain = ServletWebApp.get().initializeChain(uri);

                if (chain.isValid()) {
                    handleHttpServletRequest(ctx, request, chain);
                } else if (ServletWebApp.get().getStaticResourcesFolder() != null) {
                    handleStaticResourceRequest(ctx, request);
                } else {
                    throw new NettyServletRuntimeException("No handler found for uri: " + request.uri());
                }
            } else {
                ctx.fireChannelRead(e);
            }
        } else {
            ctx.fireChannelRead(e);
        }
    }

    protected void handleHttpServletRequest(ChannelHandlerContext ctx, HttpRequest request, FilterChainImpl chain)
            throws Exception {
        // 执行前置请求拦截器。
        interceptOnRequestReceived(ctx, request);

        // 将Netty HTTP请求对象适配为Servlet请求对象。
        HttpServletRequestImpl req = buildHttpServletRequest(request, chain);

        // 将Netty HTTP响应对象适配为Servlet响应对象。
        DefaultFullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK);
        HttpServletResponseImpl resp = buildHttpServletResponse(response);
        
        // 执行过滤器链。
        chain.doFilter(req, resp);

        // 执行后置请求拦截器。
        interceptOnRequestSuccessed(ctx, request, response);

        resp.getWriter().flush();
        
        boolean keepAlive = HttpUtil.isKeepAlive(request);
        if (keepAlive) {
            // Add 'Content-Length' header only for a keep-alive connection.
            response.headers().set(CONTENT_LENGTH, response.content().readableBytes());
            // Add keep alive header as per:
            // -
            // http://www.w3.org/Protocols/HTTP/1.1/draft-ietf-http-v11-spec-01.html#Connection
            response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        }
        
        // 处理异步请求
        if (req.isAsyncSupported() && req.isAsyncStarted()) {
            ctx.fireChannelRead(resp);
        } else {
            // write response...
            ChannelFuture future = ctx.channel().writeAndFlush(response);

            if (!keepAlive) {
                future.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    protected void handleStaticResourceRequest(ChannelHandlerContext ctx, HttpRequest request) throws Exception {
        if (request.method() != GET) {
            sendError(ctx, METHOD_NOT_ALLOWED);
            return;
        }

        String uri = Utils.sanitizeUri(request.uri());
        final String path = (uri != null ? ServletWebApp.get().getStaticResourcesFolder().getAbsolutePath()
                + File.separator + uri : null);

        if (path == null) {
            sendError(ctx, FORBIDDEN);
            return;
        }

        File file = new File(path);
        if (file.isHidden() || !file.exists()) {
            sendError(ctx, NOT_FOUND);
            return;
        }
        if (!file.isFile()) {
            sendError(ctx, FORBIDDEN);
            return;
        }

        RandomAccessFile raf;
        try {
            raf = new RandomAccessFile(file, "r");
        } catch (FileNotFoundException fnfe) {
            sendError(ctx, NOT_FOUND);
            return;
        }

        long fileLength = raf.length();

        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
        HttpUtil.setContentLength(response, fileLength);

        Channel ch = ctx.channel();

        // Write the initial line and the header.
        ch.write(response);

        // Write the content.
        ChannelFuture writeFuture;
        if (isSslChannel(ch)) {
            // Cannot use zero-copy with HTTPS.
            writeFuture = ch.write(new ChunkedFile(raf, 0, fileLength, 8192));
        } else {
            // No encryption - use zero-copy.
            final FileRegion region = new DefaultFileRegion(raf.getChannel(), 0, fileLength);
            writeFuture = ch.write(region);
            writeFuture.addListener(new ChannelProgressiveFutureListener() {

                @Override
                public void operationProgressed(ChannelProgressiveFuture channelProgressiveFuture, long current,
                        long total) throws Exception {
                    System.out.printf("%s: %d / %d (+%d)%n", path, current, total, total);
                }

                @Override
                public void operationComplete(ChannelProgressiveFuture channelProgressiveFuture) throws Exception {
                    region.release();
                }
            });
        }

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        log.error("Unexpected exception from downstream.", cause);

        Channel ch = ctx.channel();
        if (cause instanceof IllegalArgumentException) {
            ch.close();
        } else {
            if (cause instanceof TooLongFrameException) {
                sendError(ctx, BAD_REQUEST);
                return;
            }

            if (ch.isActive()) {
                sendError(ctx, INTERNAL_SERVER_ERROR);
            }

        }

    }

    private void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
        String text = "Failure: " + status.toString() + "\r\n";
        ByteBuf byteBuf = Unpooled.buffer();
        byte[] bytes = null;
        try {
            bytes = text.getBytes("utf-8");
            byteBuf.writeBytes(bytes);
        } catch (UnsupportedEncodingException e) {
        }

        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, byteBuf);
        HttpHeaders headers = response.headers();

        headers.add(CONTENT_TYPE, "text/plain;charset=utf-8");
        headers.add(CACHE_CONTROL, "no-cache");
        headers.add(PRAGMA, "No-cache");
        headers.add(SERVER, ServerConfig.getServerName());
        headers.add(CONTENT_LENGTH, byteBuf.readableBytes());
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
    }

    private void interceptOnRequestReceived(ChannelHandlerContext ctx, HttpRequest request) {
        if (interceptors != null) {
            for (HttpServletInterceptor interceptor : interceptors) {
                interceptor.onRequestReceived(ctx, request);
            }
        }

    }

    private void interceptOnRequestSuccessed(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response) {
        if (interceptors != null) {
            for (HttpServletInterceptor interceptor : interceptors) {
                interceptor.onRequestSuccessed(ctx, request, response);
            }
        }

    }

    protected HttpServletResponseImpl buildHttpServletResponse(FullHttpResponse response) {
        return new HttpServletResponseImpl(response);
    }

    protected HttpServletRequestImpl buildHttpServletRequest(HttpRequest request, FilterChainImpl chain) {
        return new HttpServletRequestImpl(request, chain);
    }

    private boolean isSslChannel(Channel ch) {
        return ch.pipeline().get(SslHandler.class) != null;
    }

    public String getUriPrefix() {
        return uriPrefix;
    }

    public void setUriPrefix(String uriPrefix) {
        this.uriPrefix = uriPrefix;
    }
}
