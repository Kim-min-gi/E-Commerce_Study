package com.study.ecommerce.exception;

import com.study.ecommerce.response.ErrorResponse;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class CustomExceptionHandle {

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


    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();

        Map<String, String> errors = new HashMap<>();

        for (FieldError s : bindingResult.getFieldErrors()){
            errors.put(s.getField(),s.getDefaultMessage());
        }

        ErrorResponse body = ErrorResponse.builder()
                .code(String.valueOf(HttpStatus.BAD_REQUEST.value()))
                .message(HttpStatus.BAD_REQUEST.name())
                .validation(errors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(body);

    }


}
