package org.framework.basic.server.servlet.exception;

public class NettyServletException extends Exception {

    private static final long serialVersionUID = 1L;

    public NettyServletException(String message, Throwable cause) {
        super(message, cause);
    }

    public NettyServletException(String message) {
        super(message);
    }

}
