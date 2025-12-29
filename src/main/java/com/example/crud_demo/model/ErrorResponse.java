package com.example.crud_demo.model;

public class ErrorResponse {

    public long timestamp;
    public int status;
    public String message;


    public  ErrorResponse(long timestamp, int status, String message) {
        this.timestamp = timestamp;
        this.status = status;
        this.message = message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
