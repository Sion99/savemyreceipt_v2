package com.smr.savemyreceipt_v2.domain;

import com.smr.savemyreceipt_v2.enums.Authority;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends AuditingTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    private String email;

    private String name;

    private String profileUri;

    private String oauth2Id;

    private String spreadSheetToken;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id")
    private Group group;

    @Builder
    public Member(String email, String name, String profileUri, String oauth2Id) {
        this.email = email;
        this.name = name;
        this.profileUri = profileUri;
        this.oauth2Id = oauth2Id;
        this.authority = Authority.ROLE_USER;
    }

    public void joinGroup(Group group) {
        this.group = group;
    }

    public void changeOauth2Id(String oauth2Id) {
        this.oauth2Id = oauth2Id;
    }



}
