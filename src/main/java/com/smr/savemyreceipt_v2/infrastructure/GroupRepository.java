package com.smr.savemyreceipt_v2.infrastructure;

import com.smr.savemyreceipt_v2.domain.Group;
import com.smr.savemyreceipt_v2.domain.Member;
import com.smr.savemyreceipt_v2.exception.ErrorStatus;
import com.smr.savemyreceipt_v2.exception.model.NotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    List<Group> findByNameContaining(String keyword);

    List<Group> findByMember(Member member);

    default Group getGroupById(Long id) {
        return findById(id).orElseThrow(
            () -> new NotFoundException(ErrorStatus.GROUP_NOT_FOUND,
                ErrorStatus.GROUP_NOT_FOUND.getMessage())
        );
    }

}
