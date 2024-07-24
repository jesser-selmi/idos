package com.IDOSdigital.userManagement.utils;

import java.util.List;

public class EntityResponse {
    private String message;
    private int status;
    private List<ErrorResponse.ValidationError> errors;

    public EntityResponse() {
    }

    public EntityResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<ErrorResponse.ValidationError> getErrors() {
        return errors;
    }

    public void setErrors(List<ErrorResponse.ValidationError> errors) {
        this.errors = errors;
    }
}
