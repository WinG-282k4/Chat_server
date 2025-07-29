package com.example.chatserver.common;

public class ApiResponse {
    private String status;
    private String message;
    private Object metadata;

    public ApiResponse(String status, String message, Object metadata) {
        this.status = status;
        this.message = message;
        this.metadata = metadata;
    }

    // Getters and Setters

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

    public Object getMetadata() {
        return metadata;
    }

    public void setMetadata(Object metadata) {
        this.metadata = metadata;
    }
}
