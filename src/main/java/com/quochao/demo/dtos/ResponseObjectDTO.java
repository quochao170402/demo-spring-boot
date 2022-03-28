package com.quochao.demo.dtos;

import java.io.Serializable;

public class ResponseObjectDTO implements Serializable {
    private String status;
    private String message;
    private Object data;

    public ResponseObjectDTO() {
    }

    public ResponseObjectDTO(String status, String message, Object data) {
        this.status = status;
        this.message = message;
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

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
