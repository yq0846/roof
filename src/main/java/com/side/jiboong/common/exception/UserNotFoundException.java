package com.side.jiboong.common.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException() {
        super("회원 정보를 찾을 수 없습니다.");
    }

}
