package com.smr.savemyreceipt_v2.DTO.member.response;

import com.smr.savemyreceipt_v2.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberDetailResponseDto {

    private Long id;
    private String email;
    private String name;
    private String profileUri;

    public static MemberDetailResponseDto convertToDto(Member member) {
        return MemberDetailResponseDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .profileUri(member.getProfileUri())
                .build();
    }

}
