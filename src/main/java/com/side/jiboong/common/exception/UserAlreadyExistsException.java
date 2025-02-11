package com.side.jiboong.common.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }

    public UserAlreadyExistsException() {
        this("이미 가입된 사용자 입니다.");
    }
}
