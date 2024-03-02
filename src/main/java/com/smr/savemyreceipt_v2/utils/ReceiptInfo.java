package com.smr.savemyreceipt_v2.utils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReceiptInfo {

    @JsonProperty("purchase_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate purchaseDate;

    @JsonProperty("total_price")
    private Long totalPrice;

    @Builder
    public ReceiptInfo(LocalDate purchaseDate, Long totalPrice) {
        this.purchaseDate = purchaseDate;
        this.totalPrice = totalPrice;
    }
}
