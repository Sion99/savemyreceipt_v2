package com.smr.savemyreceipt_v2.controller;

import com.smr.savemyreceipt_v2.DTO.ApiResponseDto;
import com.smr.savemyreceipt_v2.DTO.receipt.response.ReceiptDetailResponseDto;
import com.smr.savemyreceipt_v2.DTO.receipt.response.ReceiptListResponseDto;
import com.smr.savemyreceipt_v2.exception.ErrorStatus;
import com.smr.savemyreceipt_v2.exception.SuccessStatus;
import com.smr.savemyreceipt_v2.service.ReceiptService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/receipt")
@RequiredArgsConstructor
@SecurityRequirement(name = "Access Token")
@Tag(name = "Receipt", description = "영수증 관리 API")
public class ReceiptController {

    private final ReceiptService receiptService;

    @Operation(summary = "영수증 조회", description = "영수증 리스트를 조회합니다.")
    @GetMapping
    public ApiResponseDto<ReceiptListResponseDto> getReceiptList(
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @RequestParam(required = false, defaultValue = "1") int page){
        return ApiResponseDto.success(SuccessStatus.GET_RECEIPT_LIST_SUCCESS, receiptService.getReceiptList(user.getUsername(), page));
    }

    @Operation(summary = "영수증 총 개수 조회", description = "영수증 총 개수를 조회합니다.")
    @GetMapping("/total")
    public ApiResponseDto<Integer> getTotalReceiptCount(
        @Parameter(hidden = true) @AuthenticationPrincipal User user) {
        return ApiResponseDto.success(SuccessStatus.GET_RECEIPT_LIST_SUCCESS, receiptService.getTotalReceiptCount(user.getUsername()));
    }

    @Operation(summary = "영수증 상세 조회", description = "영수증 상세 정보를 조회합니다.")
    @GetMapping("/detail/{receiptId}")
    public ApiResponseDto<ReceiptDetailResponseDto> getReceiptInfo(
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @Parameter(description = "영수증 ID") @PathVariable Long receiptId) {
        return ApiResponseDto.success(SuccessStatus.GET_RECEIPT_INFO_SUCCESS, receiptService.getReceiptInfo(user.getUsername(), receiptId));
    }

    @Operation(summary = "영수증 업로드", description = "영수증을 업로드하여 서버에 저장하고 분석합니다.")
    @PostMapping("/upload/{groupId}")
    public ApiResponseDto<?> uploadReceipt(
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @RequestPart MultipartFile file,
        @PathVariable Long groupId) {
        if (file.isEmpty()) {
            return ApiResponseDto.error(ErrorStatus.IMAGE_NOT_FOUND, ErrorStatus.IMAGE_NOT_FOUND.getMessage());
        }
        return ApiResponseDto.success(SuccessStatus.RECEIPT_UPLOAD_SUCCESS, receiptService.uploadReceipt(user.getUsername(), file, groupId));
    }

}
