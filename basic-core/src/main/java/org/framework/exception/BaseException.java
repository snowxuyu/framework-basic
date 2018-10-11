package org.framework.exception;

import org.apache.commons.lang3.StringUtils;

/**
 * Copyright @ 2016QIANLONG.
 * All right reserved.
 * Class Name : org.framework.exception
 * Description :
 * Author : snowxuyu
 * Date : 2016/12/5
 */

public class BaseException extends RuntimeException {

    private static final long serialVersionUID = -3088104317098344394L;
    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        if (StringUtils.isBlank(message)) {
            message = super.getMessage();
        }
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public BaseException() {
        super("unknown");
    }

    public BaseException(String message) {
        super(message);
        this.message = message;
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
        this.message = message;
    }

    public BaseException(String code,  String message) {
        super(message);
        this.code = code;
        this.message = message;
    }

    public BaseException(String code,  String message, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.message = message;
    }


    public BaseException(Throwable cause) {
        super("unknown", cause);
    }
}