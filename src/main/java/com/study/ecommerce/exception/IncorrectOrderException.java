package com.study.ecommerce.exception;

public class IncorrectOrderException extends CommonException {
    private static final String MESSAGE = "잘못된 주문 입니다. 다시 시도해주세요.";

    public IncorrectOrderException(){
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 403;
    }
}
