package com.smr.savemyreceipt_v2.DTO.receipt.request;

import java.time.LocalDate;
import lombok.Getter;

@Getter
public class ReceiptUpdateRequestDto {

    private String category;
    private String description;
    private String memo;
    private LocalDate purchaseDate;
    private Long price;
}
