package com.smr.savemyreceipt_v2.infrastructure;

import com.smr.savemyreceipt_v2.domain.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

}
