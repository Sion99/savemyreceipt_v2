package com.smr.savemyreceipt_v2.domain;

import com.smr.savemyreceipt_v2.utils.ReceiptInfo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;

@Entity
@Getter
@NoArgsConstructor
@SQLDelete(sql = "UPDATE receipt SET is_deleted = true WHERE receipt_id = ?")
public class Receipt extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "receipt_id")
    private Long id;

    private String imageUri;

    private String category;

    private String description;

    private String memo;

    private LocalDate purchaseDate;

    private Long price;

    private boolean isChecked = false;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "group_id")
    private Group group;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    @Builder
    public Receipt(String imageUri, String category, String description, String memo, LocalDate purchaseDate, Long price,
                   Member member, Group group) {
        this.imageUri = imageUri;
        this.category = category;
        this.description = description;
        this.memo = memo;
        this.purchaseDate = purchaseDate;
        this.price = price;
        this.member = member;
        this.group = group;
    }

    private void check() {
        this.isChecked = true;
    }

    public void updateReceiptInfo(ReceiptInfo receiptInfo) {
        this.purchaseDate = receiptInfo.getPurchaseDate();
        this.price = receiptInfo.getTotalPrice();
    }

}
