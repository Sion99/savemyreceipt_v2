package com.smr.savemyreceipt_v2.DTO.member.request;

import lombok.Getter;

@Getter
public class MemberRoleRequestDto {

    private final String email;
    private final String role;

    public MemberRoleRequestDto(String email, String role) {
        this.email = email;
        this.role = role;
    }
}
