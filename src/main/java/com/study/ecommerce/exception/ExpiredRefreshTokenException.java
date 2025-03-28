package com.study.ecommerce.exception;

public class ExpiredRefreshTokenException extends CommonException {

    private static final String MESSAGE = "만료된 RefreshToken 입니다.";

    public ExpiredRefreshTokenException(){
        super(MESSAGE);
    }

    @Override
    public int getStatusCode() {
        return 401;
    }

}
