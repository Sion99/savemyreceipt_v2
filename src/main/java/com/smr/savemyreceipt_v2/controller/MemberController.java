package com.smr.savemyreceipt_v2.controller;

import com.smr.savemyreceipt_v2.DTO.ApiResponseDto;
import com.smr.savemyreceipt_v2.DTO.member.request.MemberUpdateRequestDto;
import com.smr.savemyreceipt_v2.DTO.member.response.MemberDetailResponseDto;
import com.smr.savemyreceipt_v2.exception.ErrorStatus;
import com.smr.savemyreceipt_v2.exception.SuccessStatus;
import com.smr.savemyreceipt_v2.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@SecurityRequirement(name = "Access Token")
@Tag(name = "Member", description = "회원 관리 API")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "회원 정보 조회", description = "회원 정보를 조회합니다.")
    @GetMapping
    public ApiResponseDto<MemberDetailResponseDto> getMemberInfo(
        @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        return ApiResponseDto.success(SuccessStatus.GET_USER_INFO_SUCCESS, memberService.getMemberInfo(user.getUsername()));
    }

    @Operation(summary = "회원 정보 수정", description = "회원 정보를 수정합니다. v1 기준 이름 수정")
    @PostMapping("/update")
    public ApiResponseDto<MemberDetailResponseDto> updateMemberInfo(
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @RequestBody MemberUpdateRequestDto memberUpdateRequestDto) {
        return ApiResponseDto.success(SuccessStatus.UPDATE_USER_INFO_SUCCESS, memberService.updateMemberInfo(memberUpdateRequestDto, user.getUsername()));
    }

    @Operation(summary = "회원 프로필 사진 변경", description = "회원 프로필 사진을 변경합니다.")
    @PostMapping("/update/profile")
    public ApiResponseDto<?> updateProfileImage(
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @RequestPart MultipartFile file) {
        if (file.isEmpty()) {
            return ApiResponseDto.error(ErrorStatus.IMAGE_NOT_FOUND, ErrorStatus.IMAGE_NOT_FOUND.getMessage());
        }
        return ApiResponseDto.success(SuccessStatus.UPDATE_USER_INFO_SUCCESS, memberService.updateProfileImage(file, user.getUsername()));
    }
}
