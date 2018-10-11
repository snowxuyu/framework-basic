package org.framework.basic.server.servlet.session;

import org.framework.basic.server.servlet.ServletWebApp;
import org.framework.basic.server.servlet.impl.HttpSessionImpl;

import javax.servlet.http.HttpSession;

public class HttpSessionThreadLocal {

    public static final ThreadLocal<HttpSession> sessionThreadLocal = new ThreadLocal<HttpSession>();

    private static HttpSessionStore sessionStore;

    public static HttpSessionStore getSessionStore() {
        return sessionStore;
    }

    public static void setSessionStore(HttpSessionStore store) {
        sessionStore = store;
    }

    public static void set(HttpSession session) {
        sessionThreadLocal.set(session);
    }

    public static void unset() {
        sessionThreadLocal.remove();
    }

    public static HttpSessionImpl get() {
        HttpSessionImpl session = (HttpSessionImpl)sessionThreadLocal.get();
        if (session != null) {
            session.touch();
        }
        return session;
    }

    public static HttpSessionImpl getOrCreate() {
        if (HttpSessionThreadLocal.get() == null) {
            if (sessionStore == null) {
                sessionStore = new DefaultHttpSessionStore();
            }

            HttpSession newSession = sessionStore.createSession();
            newSession.setMaxInactiveInterval(ServletWebApp.get().getWebappConfig().getSessionTimeout());
            sessionThreadLocal.set(newSession);
        }
        
        return get();
    }

}
