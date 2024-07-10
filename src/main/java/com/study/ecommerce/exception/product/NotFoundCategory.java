package com.study.ecommerce.exception;

public class NotFoundCategory extends CommonException{

    private static final String MESSAGE = "카테고리를 찾을 수 없습니다.";

    public NotFoundCategory(){
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }

}
