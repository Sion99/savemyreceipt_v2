package com.smr.savemyreceipt_v2.infrastructure;

import com.smr.savemyreceipt_v2.domain.Member;
import com.smr.savemyreceipt_v2.exception.ErrorStatus;
import com.smr.savemyreceipt_v2.exception.model.NotFoundException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findByOauth2Id(String oauth2Id);

    default Member getMemberByEmail(String email) {
        return this.findByEmail(email).orElseThrow(
            () -> new NotFoundException(ErrorStatus.MEMBER_NOT_FOUND,
                ErrorStatus.MEMBER_NOT_FOUND.getMessage())
        );
    }



}
