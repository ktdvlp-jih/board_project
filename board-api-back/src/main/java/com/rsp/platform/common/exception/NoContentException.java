package com.rsp.platform.common.exception;

/*
* Author : 전일훈
* Date : 2025-07-22
* 에러 응답 dto
* */
public class NoContentException extends RuntimeException {
    public NoContentException(String message) {
        super(message);
    }
}
