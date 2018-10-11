package org.framework.basic.server.servlet.interceptor;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.ServerCookieEncoder;
import org.framework.basic.server.servlet.impl.HttpSessionImpl;
import org.framework.basic.server.servlet.session.HttpSessionStore;
import org.framework.basic.server.servlet.session.HttpSessionThreadLocal;
import org.framework.basic.server.util.Utils;

import javax.servlet.http.HttpSession;
import java.util.Collection;

import static io.netty.handler.codec.http.HttpHeaders.Names.SET_COOKIE;

public class HttpSessionInterceptor implements HttpServletInterceptor {

    private boolean sessionRequestedByCookie = false;

    public HttpSessionInterceptor(HttpSessionStore sessionStore) {
        HttpSessionThreadLocal.setSessionStore(sessionStore);
    }

    @Override
    public void onRequestReceived(ChannelHandlerContext ctx, HttpRequest request) {

        HttpSessionThreadLocal.unset();


        Collection<io.netty.handler.codec.http.cookie.Cookie> cookies = Utils.getCookies(HttpSessionImpl.SESSION_ID_KEY, request);
        if (cookies != null) {
            for (io.netty.handler.codec.http.cookie.Cookie cookie : cookies) {
                String jsessionId = cookie.value();
                HttpSession s = HttpSessionThreadLocal.getSessionStore().findSession(jsessionId);
                if (s != null) {
                    HttpSessionThreadLocal.set(s);
                    this.sessionRequestedByCookie = true;
                    break;
                }
            }
        }
    }

    @Override
    public void onRequestSuccessed(ChannelHandlerContext ctx, HttpRequest request, HttpResponse response) {

        HttpSession s = HttpSessionThreadLocal.get();
        if (s != null && !this.sessionRequestedByCookie) {
            HttpHeaders.addHeader(response, SET_COOKIE, ServerCookieEncoder.encode(HttpSessionImpl.SESSION_ID_KEY, s.getId()));
        }

    }

    @Override
    public void onRequestFailed(ChannelHandlerContext ctx, Throwable e, HttpResponse response) {
        this.sessionRequestedByCookie = false;
        HttpSessionThreadLocal.unset();
    }

}
