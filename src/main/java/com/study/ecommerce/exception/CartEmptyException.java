package com.study.ecommerce.exception;

public class CartEmptyException extends CommonException{

    private static final String MESSAGE = "카트가 비어있습니다.";

    public CartEmptyException(){
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }

}
