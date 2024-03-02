package com.smr.savemyreceipt_v2.infrastructure;

import com.smr.savemyreceipt_v2.domain.Group;
import com.smr.savemyreceipt_v2.domain.Member;
import com.smr.savemyreceipt_v2.domain.Receipt;
import com.smr.savemyreceipt_v2.exception.ErrorStatus;
import com.smr.savemyreceipt_v2.exception.model.NotFoundException;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReceiptRepository extends JpaRepository<Receipt, Long> {

    Page<Receipt> findByGroup(Group group, Pageable pageable);

    List<Receipt> findByMemberId(Long memberId);

    Page<Receipt> findByMember(Member member, Pageable pageable);

    Integer countByMember(Member member);

    Integer countByGroup(Group group);

    default Receipt getReceiptById(Long id) {
        return findById(id).orElseThrow(
            () -> new NotFoundException(ErrorStatus.RECEIPT_NOT_FOUND,
                ErrorStatus.RECEIPT_NOT_FOUND.getMessage())
        );
    }

}
