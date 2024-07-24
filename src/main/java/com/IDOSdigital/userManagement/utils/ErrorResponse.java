package com.IDOSdigital.userManagement.utils;

import java.util.List;

public class ErrorResponse {
    private final int status;
    private final String message;
    private List<ValidationError> errors;

    public ErrorResponse(int status, String message, List<ValidationError> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public static class ValidationError {
        private final String field;
        private final String message;

        public ValidationError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }
    }
}
