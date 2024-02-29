package com.smr.savemyreceipt_v2.exception.model;

import com.smr.savemyreceipt_v2.exception.ErrorStatus;

public class NotFoundException extends CustomException {

    public NotFoundException(ErrorStatus errorStatus,
        String message) {
        super(errorStatus, message);
    }

}
