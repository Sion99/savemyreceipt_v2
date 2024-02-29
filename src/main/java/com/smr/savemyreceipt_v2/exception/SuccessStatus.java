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

    ;

    private final HttpStatus httpStatus;
    private final String message;
}
