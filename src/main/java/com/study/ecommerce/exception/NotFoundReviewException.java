package com.study.ecommerce.exception;

public class NotFoundReviewException extends CommonException {

    private static final String MESSAGE = "리뷰를 찾을 수 없습니다.";

    public NotFoundReviewException(){
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
