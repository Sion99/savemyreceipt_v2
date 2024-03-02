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
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "Invalid file type."),
    GOOGLE_CLOUD_STORAGE_ERROR(HttpStatus.BAD_REQUEST, "Google Cloud Storage error."),
    GOOGLE_VISION_API_ERROR(HttpStatus.BAD_REQUEST, "Google Vision API error."),

    /*
     * 401 Unauthorized
     */

    /*
     * 403 Forbidden
     */
    RECEIPT_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "You are not authorized to access this receipt."),
    NOT_ADMIN(HttpStatus.FORBIDDEN, "You are not an admin."),

    /*
     * 404 Not Found
     */
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "Member not found."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "Image not found."),
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "Group not found."),
    RECEIPT_NOT_FOUND(HttpStatus.NOT_FOUND, "Receipt not found."),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
