package com.smr.savemyreceipt_v2.exception.model;

import com.smr.savemyreceipt_v2.exception.ErrorStatus;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {

    private final ErrorStatus errorStatus;

    public CustomException(ErrorStatus errorStatus, String message) {
        super(message);
        this.errorStatus = errorStatus;
    }

    public int getHttpStatus() {
        return errorStatus.getHttpStatusCode();
    }

}
