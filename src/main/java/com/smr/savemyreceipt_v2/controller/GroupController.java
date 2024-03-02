package com.smr.savemyreceipt_v2.controller;

import com.smr.savemyreceipt_v2.DTO.ApiResponseDto;
import com.smr.savemyreceipt_v2.DTO.group.response.GroupResponseDto;
import com.smr.savemyreceipt_v2.DTO.member.response.MemberDetailResponseDto;
import com.smr.savemyreceipt_v2.DTO.receipt.response.ReceiptListResponseDto;
import com.smr.savemyreceipt_v2.exception.SuccessStatus;
import com.smr.savemyreceipt_v2.service.GroupService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/group")
@RequiredArgsConstructor
@SecurityRequirement(name = "Access Token")
@Tag(name = "Group", description = "그룹 관리 API")
public class GroupController {

    private final GroupService groupService;

    @Operation(summary = "그룹 조회", description = "회원이 속한 그룹을 조회합니다.")
    @GetMapping
    public ApiResponseDto<GroupResponseDto> getGroup(@AuthenticationPrincipal User user) {
        return ApiResponseDto.success(SuccessStatus.GET_GROUP_INFO_SUCCESS, groupService.getGroup(user.getUsername()));
    }

    @Operation(summary = "그룹 내 영수증 조회", description = "그룹 내 영수증 리스트를 조회합니다.")
    @GetMapping("/receipt")
    public ApiResponseDto<ReceiptListResponseDto> getReceiptListInGroup(
        @AuthenticationPrincipal User user, @RequestParam int page) {
        return ApiResponseDto.success(SuccessStatus.GET_RECEIPT_LIST_SUCCESS, groupService.getReceiptListInGroup(user.getUsername(), page));
    }

    @Operation(summary = "그룹 내 영수증 총 개수 조회", description = "그룹 내 영수증 총 개수를 조회합니다.")
    @GetMapping("/receipt/total")
    public ApiResponseDto<Integer> getTotalReceiptCountInGroup(@AuthenticationPrincipal User user) {
        return ApiResponseDto.success(SuccessStatus.GET_RECEIPT_LIST_SUCCESS, groupService.getTotalReceiptCountInGroup(user.getUsername()));
    }

    @Operation(summary = "그룹 검색하기", description = "그룹을 검색합니다.")
    @GetMapping("/search")
    public ApiResponseDto<List<GroupResponseDto>> searchGroup(@RequestParam String keyword) {
        return ApiResponseDto.success(SuccessStatus.SEARCH_GROUP_SUCCESS, groupService.searchGroup(keyword));
    }

    @Operation(summary = "그룹 멤버 조회", description = "그룹의 멤버 리스트를 조회합니다.")
    @GetMapping("/member")
    public ApiResponseDto<List<MemberDetailResponseDto>> getGroupMembers(@AuthenticationPrincipal User user) {
        return ApiResponseDto.success(SuccessStatus.GET_GROUP_MEMBER_SUCCESS, groupService.getGroupMembers(user.getUsername()));
    }

    @Operation(summary = "그룹 가입하기", description = "그룹에 가입합니다.")
    @PostMapping("/join/{groupId}")
    public ApiResponseDto<GroupResponseDto> joinGroup(@AuthenticationPrincipal User user, @PathVariable Long groupId) {
        return ApiResponseDto.success(SuccessStatus.JOIN_GROUP_SUCCESS, groupService.joinGroup(user.getUsername(), groupId));
    }
}
