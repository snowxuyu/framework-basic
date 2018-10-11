package org.framework.basic.server.servlet.exception;

public class NettyServletRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public NettyServletRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public NettyServletRuntimeException(String message) {
        super(message);
    }

}
