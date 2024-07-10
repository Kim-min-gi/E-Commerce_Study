package com.study.ecommerce.exception;

public class AlreadyExistsCategory extends CommonException{

    private static final String MESSAGE = "이미 등록된 카테고리입니다.";

    public AlreadyExistsCategory(){
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 400;
    }

}
