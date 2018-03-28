package com.jpmorgan.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class OperationResult {

    public enum Status {
        SUCCESS, FAILURE
    }

    private final Object resultValue;

    private final String message;

    private final Status status;

    private OperationResult(Object resultValue, String message, Status status) {
        this.resultValue = resultValue;
        this.message = message;
        this.status = status;
    }

    private OperationResult(Builder builder) {
        this(builder.resultValue, builder.message, builder.status);
    }

    public Object getResultValue() {
        return resultValue;
    }

    public String getMessage() {
        return message;
    }

    public Status getStatus() {
        return status;
    }

    public static class Builder {

        private final Status status;

        private Object resultValue;

        private String message;

        public Builder(Status status) {
            this.status = status;
        }

        public Builder withResultValue(Object resultValue) {
            this.resultValue = resultValue;
            return this;
        }

        public Builder withMessage(String message) {
            this.message = message;
            return this;
        }

        public OperationResult build() {
            return new OperationResult(this);
        }
    }
}
