package com.smr.savemyreceipt_v2.service;

import com.smr.savemyreceipt_v2.DTO.group.request.GroupRequestDto;
import com.smr.savemyreceipt_v2.DTO.group.response.GroupResponseDto;
import com.smr.savemyreceipt_v2.DTO.member.request.MemberRoleRequestDto;
import com.smr.savemyreceipt_v2.domain.Member;
import com.smr.savemyreceipt_v2.enums.Authority;
import com.smr.savemyreceipt_v2.exception.ErrorStatus;
import com.smr.savemyreceipt_v2.exception.model.CustomException;
import com.smr.savemyreceipt_v2.infrastructure.GroupRepository;
import com.smr.savemyreceipt_v2.infrastructure.MemberRepository;
import com.smr.savemyreceipt_v2.infrastructure.ReceiptRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;
    private final ReceiptRepository receiptRepository;

    private void checkAuthority(String email) {
        Member member = memberRepository.getMemberByEmail(email);
        if (!member.getAuthority().equals(Authority.ROLE_ADMIN)) {
            throw new CustomException(ErrorStatus.NOT_ADMIN, ErrorStatus.NOT_ADMIN.getMessage());
        }
    }

    @Transactional
    public GroupResponseDto createGroup(String email, GroupRequestDto groupRequestDto) {
        checkAuthority(email);
        return GroupResponseDto.convertToDto(groupRepository.save(groupRequestDto.toEntity()));
    }

    @Transactional
    public void changeRole(String email, MemberRoleRequestDto memberRoleRequestDto) {
        checkAuthority(email);
        Member member = memberRepository.getMemberByEmail(email);
        member.changeRole(memberRoleRequestDto.getRole());
        memberRepository.save(member);
    }

}
