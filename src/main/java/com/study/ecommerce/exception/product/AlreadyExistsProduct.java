package com.study.ecommerce.exception;

public class AlreadyExistsProduct extends CommonException{

    private static final String MESSAGE = "이미 등록된 상품입니다.";

    public AlreadyExistsProduct(){
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }
}
