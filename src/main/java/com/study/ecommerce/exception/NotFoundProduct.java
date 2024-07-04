package com.study.ecommerce.exception;

public class NotFoundProduct extends CommonException{

    private static final String MESSAGE = "상품을 찾을 수 없습니다.";

    public NotFoundProduct(){
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }

}
