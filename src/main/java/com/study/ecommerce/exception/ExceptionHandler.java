package com.study.ecommerce.exception;

import com.study.ecommerce.response.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(CommonException.class)
    public ResponseEntity<ErrorResponse> customException(CommonException commonException){
        int statusCode = commonException.getStatusCode();

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(statusCode))
                .message(commonException.getMessage())
                .validation(commonException.getValidation())
                .build();


        return ResponseEntity.status(statusCode)
                .body(body);

    }

}
