package com.smr.savemyreceipt_v2.service;

import com.smr.savemyreceipt_v2.DTO.group.response.GroupResponseDto;
import com.smr.savemyreceipt_v2.DTO.member.response.MemberDetailResponseDto;
import com.smr.savemyreceipt_v2.DTO.receipt.response.ReceiptListResponseDto;
import com.smr.savemyreceipt_v2.DTO.receipt.response.ReceiptResponseDto;
import com.smr.savemyreceipt_v2.domain.Group;
import com.smr.savemyreceipt_v2.domain.Member;
import com.smr.savemyreceipt_v2.infrastructure.GroupRepository;
import com.smr.savemyreceipt_v2.infrastructure.MemberRepository;
import com.smr.savemyreceipt_v2.infrastructure.ReceiptRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final MemberRepository memberRepository;
    private final GroupRepository groupRepository;
    private final ReceiptRepository receiptRepository;

    @Transactional(readOnly = true)
    public GroupResponseDto getGroup(String email) {
        Member member = memberRepository.getMemberByEmail(email);
        return GroupResponseDto.convertToDto(member.getGroup());
    }

    @Transactional(readOnly = true)
    public ReceiptListResponseDto getReceiptListInGroup(String email, int page) {
        Member member = memberRepository.getMemberByEmail(email);
        Pageable pageable = PageRequest.of(page, 10, Sort.by("purchaseDate").descending());

        Page<ReceiptResponseDto> receiptList = receiptRepository.findByGroup(member.getGroup(), pageable)
            .map(ReceiptResponseDto::convertToDto);
        return new ReceiptListResponseDto(receiptList.getContent(), receiptList.getTotalPages());
    }

    @Transactional(readOnly = true)
    public Integer getTotalReceiptCountInGroup(String email) {
        Member member = memberRepository.getMemberByEmail(email);
        return receiptRepository.countByGroup(member.getGroup());
    }

    @Transactional(readOnly = true)
    public List<GroupResponseDto> searchGroup(String keyword) {
        List<Group> groupList = groupRepository.findByNameContaining(keyword);
        return groupList.stream()
            .map(GroupResponseDto::convertToDto)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MemberDetailResponseDto> getGroupMembers(String email) {
        Member member = memberRepository.getMemberByEmail(email);
        List<Member> memberList = memberRepository.findByGroupId(member.getGroup().getId());
        return memberList.stream()
            .map(MemberDetailResponseDto::convertToDto)
            .collect(Collectors.toList());
    }

    @Transactional
    public GroupResponseDto joinGroup(String email, Long groupId) {
        Member member = memberRepository.getMemberByEmail(email);
        Group group = groupRepository.getGroupById(groupId);
        member.joinGroup(group);
        memberRepository.save(member);
        return GroupResponseDto.convertToDto(group);
    }
}
