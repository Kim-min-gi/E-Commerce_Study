package com.study.ecommerce.exception;

public class NotFoundCartProductException extends CommonException{

    private static final String MESSAGE = "카트에 해당 상품이 없습니다.";

    public NotFoundCartProductException(){
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }

}
