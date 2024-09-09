package com.study.ecommerce.exception;

public class NotFoundOrderException extends CommonException{
    private static final String MESSAGE = "찾을 수 없는 주문입니다.";

    public NotFoundOrderException(){
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }


}
