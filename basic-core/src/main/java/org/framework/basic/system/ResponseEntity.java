package org.framework.basic.system;

import lombok.ToString;

import java.io.Serializable;

/**
 * Created by snow on 2015/7/12. 所有方法的返回类型
 */
@ToString(callSuper = true)
public class ResponseEntity implements Serializable {

    private static final long serialVersionUID = 7729482447094472913L;
    private Object data; // 业务数据
    private String status; // 接口状态
    private String message; // 业务消息
    private String code; //业务编码

    public ResponseEntity() {

    }

    public ResponseEntity(Object data) {
        this.data = data;
    }

    public ResponseEntity(Object data, String status) {
        this.data = data;
        this.status = status;
    }

    public ResponseEntity(Object data, String status, String message) {
        this.data = data;
        this.status = status;
        this.message = message;
    }


    public ResponseEntity(Object data, String status, String code, String message) {
        this.data = data;
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
