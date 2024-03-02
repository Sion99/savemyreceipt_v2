package com.smr.savemyreceipt_v2.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessStatus {

    /*
     * 200 OK
     */
    OK(HttpStatus.OK, "OK"),
    LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공"),
    GET_USER_INFO_SUCCESS(HttpStatus.OK, "유저 정보 조회 성공"),

    /*
     * 201 CREATED
     */
    SIGNUP_SUCCESS(HttpStatus.CREATED, "회원가입 성공"),
    IMAGE_UPLOAD_SUCCESS(HttpStatus.CREATED, "이미지 업로드 성공"),
    UPDATE_USER_INFO_SUCCESS(HttpStatus.CREATED, "유저 정보 수정 성공"),


    ;

    private final HttpStatus httpStatus;
    private final String message;
}
