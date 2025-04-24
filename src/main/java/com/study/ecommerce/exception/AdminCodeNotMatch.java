package com.study.ecommerce.exception;

public class AdminCodeNotMatch extends CommonException{


    private static final String MESSAGE = "잘못된 코드입니다.";

    public AdminCodeNotMatch(String filedName,String message){
        super(MESSAGE);
        addValidation(filedName,message);

    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
