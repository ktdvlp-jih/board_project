package com.rsp.platform.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


/*
 * Author : 전일훈
 * Date : 2025-07-22
 * 에러 응답 dto
 * */

@Getter
@Setter
@AllArgsConstructor
public class ApiError {
    private int status;
    private String error;
    private String message;
}
