package com.jpmorgan.controller.util;

public class OperationResultDTO {

    private Object resultValue;

    private String message;

    private String status;

    public OperationResultDTO() { }

    public Object getResultValue() {
        return resultValue;
    }

    public void setResultValue(Object resultValue) {
        this.resultValue = resultValue;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
