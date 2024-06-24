package com.study.ecommerce.exception;

public class AdminCodeNotMatch extends CommonException{


    private static final String MESSAGE = "잘못된 코드입니다.";

    public AdminCodeNotMatch(){
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
