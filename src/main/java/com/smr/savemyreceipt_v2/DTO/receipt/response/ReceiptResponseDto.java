package com.smr.savemyreceipt_v2.DTO.receipt.response;

import com.smr.savemyreceipt_v2.domain.Receipt;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReceiptResponseDto {

    private Long id;
    private String imageUri;
    private String category;
    private String description;
    private LocalDate purchaseDate;
    private Long price;
    private boolean isChecked;

    public static ReceiptResponseDto convertToDto(Receipt receipt) {
        return ReceiptResponseDto.builder()
                .id(receipt.getId())
                .category(receipt.getCategory())
                .description(receipt.getDescription())
                .purchaseDate(receipt.getPurchaseDate())
                .price(receipt.getPrice())
                .isChecked(receipt.isChecked())
                .build();
    }
}
