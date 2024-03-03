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
    GET_RECEIPT_INFO_SUCCESS(HttpStatus.OK, "영수증 정보 조회 성공"),
    GET_RECEIPT_LIST_SUCCESS(HttpStatus.OK, "영수증 리스트 조회 성공"),
    GET_GROUP_INFO_SUCCESS(HttpStatus.OK, "그룹 정보 조회 성공"),
    CHANGE_ROLE_SUCCESS(HttpStatus.OK, "권한 변경 성공"),
    GET_GROUP_MEMBER_SUCCESS(HttpStatus.OK, "그룹 멤버 조회 성공"),
    SEARCH_GROUP_SUCCESS(HttpStatus.OK, "그룹 검색 성공"),
    JOIN_GROUP_SUCCESS(HttpStatus.OK, "그룹 가입 성공"),

    /*
     * 201 CREATED
     */
    SIGNUP_SUCCESS(HttpStatus.CREATED, "회원가입 성공"),
    IMAGE_UPLOAD_SUCCESS(HttpStatus.CREATED, "이미지 업로드 성공"),
    UPDATE_USER_INFO_SUCCESS(HttpStatus.CREATED, "유저 정보 수정 성공"),
    RECEIPT_UPLOAD_SUCCESS(HttpStatus.CREATED, "영수증 업로드 성공"),
    CREATE_GROUP_SUCCESS(HttpStatus.CREATED, "그룹 생성 성공"),
    UPDATE_RECEIPT_INFO_SUCCESS(HttpStatus.CREATED, "영수증 정보 수정 성공"),


    ;

    private final HttpStatus httpStatus;
    private final String message;
}
