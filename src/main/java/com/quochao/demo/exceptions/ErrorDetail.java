package com.quochao.demo.exceptions;

import java.util.Date;

public class ErrorDetail {
    private Date timestamp;
    private String message;
    private Object details;

    public ErrorDetail() {
    }

    public ErrorDetail(Date timestamp, String message, Object details) {
        this.timestamp = timestamp;
        this.message = message;
        this.details = details;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getDetails() {
        return details;
    }

    public void setDetails(Object details) {
        this.details = details;
    }
}
