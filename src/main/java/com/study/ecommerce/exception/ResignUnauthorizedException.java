package com.study.ecommerce.exception;

public class ResignUnauthorizedException extends CommonException{

    private static final String MESSAGE = "비밀번호가 틀렸거나 잘못된 사용자입니다.";

    public ResignUnauthorizedException(){
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }


}
