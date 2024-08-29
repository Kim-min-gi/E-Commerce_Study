package com.study.ecommerce.exception;

public class NotFoundMemberException extends CommonException{

    private static final String MESSAGE = "찾을 수 없는 유저입니다.";

    public NotFoundMemberException(){
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
