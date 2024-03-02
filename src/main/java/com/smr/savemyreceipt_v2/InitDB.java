package com.smr.savemyreceipt_v2;

import com.smr.savemyreceipt_v2.domain.Group;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init() {
        initService.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit() {

            Group group = Group.builder()
                .name("포항대흥교회")
                .department("1청년부")
                .description("포항대흥교회 1청년부 디모데입니다.")
                .build();
            em.persist(group);
        }
    }

}
