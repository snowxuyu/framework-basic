package org.framework.exception;

/**
 * Copyright @ 2016QIANLONG.
 * All right reserved.
 * Class Name : org.framework.exception
 * Description :
 * Author : snowxuyu
 * Date : 2016/12/5
 */

public class IllegalArgumentException extends BaseException {
    private static final long serialVersionUID = -1398555596273694035L;

    public IllegalArgumentException() {
        super("illegalArgument");
    }

    public IllegalArgumentException(String messageKey, Throwable cause) {
        super(messageKey, cause);
    }

    public IllegalArgumentException(String messageKey) {
        super(messageKey);
    }

    public IllegalArgumentException(Throwable cause) {
        super("illegalArgument", cause);
    }
}
