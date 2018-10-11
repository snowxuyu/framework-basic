package org.framework.basic.system;

import org.framework.basic.constant.Constants;

/**
 * Created with IntelliJ IDEA.
 * User: snowxuyu
 * Date: 2016/8/12
 * Time: 20:02
 */
public abstract class BaseResponse {

    public static final <T> ResponseEntity buildSuccess() {
        return buildSuccess(null);
    }

    public static final <T> ResponseEntity buildSuccess(String message) {
        return buildSuccess(null, message);
    }

    public static final <T> ResponseEntity buildSuccess(T t, String message) {
        return buildSuccess(t, Constants.System.OK_CODE, message);
    }

    public static final <T> ResponseEntity buildSuccess(T t, String code, String message) {
        ResponseEntity result = new ResponseEntity();
        result.setStatus(Constants.System.SUCCESSS);
        result.setCode(code);
        result.setMessage(message);
        result.setData(t);
        return result;
    }


    public static final <T> ResponseEntity buildError() {
        return buildError(null);
    }

    public static final <T> ResponseEntity buildError(String message) {
        return buildError(null, message);
    }

    public static final <T> ResponseEntity buildError(String code, String message) {
        return buildError(null, code, message);
    }

    public static final <T> ResponseEntity buildError(T t, String code, String message) {
        ResponseEntity result = new ResponseEntity();
        result.setStatus(Constants.System.ERROR);
        result.setData(t);
        result.setCode(code);
        result.setMessage(message);
        return result;
    }


}
