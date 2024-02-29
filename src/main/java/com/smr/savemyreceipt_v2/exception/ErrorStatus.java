package com.smr.savemyreceipt_v2.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorStatus {

    /*
     * 400 Bad Request
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad Request"),
    WRONG_LOGIN_INFO_EXCEPTION(HttpStatus.BAD_REQUEST, "Wrong login information."),

    /*
     * 401 Unauthorized
     */

    /*
     * 403 Forbidden
     */

    /*
     * 404 Not Found
     */
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "Member not found.")
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
