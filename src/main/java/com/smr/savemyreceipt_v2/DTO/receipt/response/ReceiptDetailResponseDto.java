package com.smr.savemyreceipt_v2.DTO.receipt.response;

import com.smr.savemyreceipt_v2.domain.Receipt;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReceiptDetailResponseDto {

    private Long id;
    private String imageUri;
    private String category;
    private String description;
    private String memo;
    private Long price;
    private LocalDate purchaseDate;
    private boolean isChecked;


    public static ReceiptDetailResponseDto convertToDto(Receipt receipt) {
        return ReceiptDetailResponseDto.builder()
                .id(receipt.getId())
                .imageUri(receipt.getImageUri())
                .category(receipt.getCategory())
                .description(receipt.getDescription())
                .memo(receipt.getMemo())
                .price(receipt.getPrice())
                .purchaseDate(receipt.getPurchaseDate())
                .isChecked(receipt.isChecked())
                .build();
    }

}
