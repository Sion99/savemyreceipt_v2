package com.smr.savemyreceipt_v2.controller;

import com.smr.savemyreceipt_v2.DTO.ApiResponseDto;
import com.smr.savemyreceipt_v2.DTO.group.request.GroupRequestDto;
import com.smr.savemyreceipt_v2.DTO.group.response.GroupResponseDto;
import com.smr.savemyreceipt_v2.DTO.member.request.MemberRoleRequestDto;
import com.smr.savemyreceipt_v2.exception.SuccessStatus;
import com.smr.savemyreceipt_v2.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
@SecurityRequirement(name = "Access Token")
@Tag(name = "Admin", description = "관리자 API")
public class AdminController {

    private final AdminService adminService;

    @Operation(summary = "그룹 생성", description = "그룹을 생성합니다.")
    @PostMapping("/create")
    public ApiResponseDto<GroupResponseDto> createGroup(
        @AuthenticationPrincipal User user,
        @RequestBody GroupRequestDto groupRequestDto) {
        return ApiResponseDto.success(SuccessStatus.CREATE_GROUP_SUCCESS, adminService.createGroup(user.getUsername(), groupRequestDto));
    }

    @Operation(summary = "사용자 권한 변경", description = "사용자의 권한을 변경합니다.")
    @PostMapping("/change-role")
    public ApiResponseDto<?> changeRole(
        @AuthenticationPrincipal User user,
        @RequestBody MemberRoleRequestDto memberRoleRequestDto) {
        adminService.changeRole(user.getUsername(), memberRoleRequestDto);
        return ApiResponseDto.success(SuccessStatus.CHANGE_ROLE_SUCCESS, SuccessStatus.CHANGE_ROLE_SUCCESS.getMessage());
    }
}
