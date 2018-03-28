package com.jpmorgan.controller.helper;

import com.jpmorgan.dto.OperationResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GenericExceptionHandler {

    @ExceptionHandler(Exception.class)
    public OperationResult handleException(Exception e) {
        return new OperationResult.Builder(OperationResult.Status.FAILURE)
                .withMessage(e.getMessage())
                .build();
    }
}
