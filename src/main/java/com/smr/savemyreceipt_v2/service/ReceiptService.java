package com.smr.savemyreceipt_v2.service;

import com.smr.savemyreceipt_v2.DTO.receipt.request.ReceiptUpdateRequestDto;
import com.smr.savemyreceipt_v2.DTO.receipt.response.ReceiptDetailResponseDto;
import com.smr.savemyreceipt_v2.DTO.receipt.response.ReceiptListResponseDto;
import com.smr.savemyreceipt_v2.DTO.receipt.response.ReceiptResponseDto;
import com.smr.savemyreceipt_v2.domain.Group;
import com.smr.savemyreceipt_v2.domain.Member;
import com.smr.savemyreceipt_v2.domain.Receipt;
import com.smr.savemyreceipt_v2.exception.ErrorStatus;
import com.smr.savemyreceipt_v2.exception.model.CustomException;
import com.smr.savemyreceipt_v2.infrastructure.GroupRepository;
import com.smr.savemyreceipt_v2.infrastructure.MemberRepository;
import com.smr.savemyreceipt_v2.infrastructure.ReceiptRepository;
import com.smr.savemyreceipt_v2.utils.DataBucketUtil;
import com.smr.savemyreceipt_v2.utils.GeminiUtil;
import com.smr.savemyreceipt_v2.utils.ReceiptInfo;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class ReceiptService {

    private final MemberRepository memberRepository;
    private final ReceiptRepository receiptRepository;
    private final GroupRepository groupRepository;
    private final DataBucketUtil dataBucketUtil;
    private final GeminiUtil geminiUtil;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;
    private final static String IMAGE_URI_PREFIX = "https://storage.googleapis.com/";


    @Transactional(readOnly = true)
    public ReceiptListResponseDto getReceiptList(String email, int page) {
        Member member = memberRepository.getMemberByEmail(email);
        Pageable pageable = PageRequest.of(page, 10, Sort.by("purchaseDate").descending());

        Page<ReceiptResponseDto> receiptList = receiptRepository.findByMember(member, pageable)
            .map(ReceiptResponseDto::convertToDto);
        return new ReceiptListResponseDto(receiptList.getContent(), receiptList.getTotalPages());
    }

    @Transactional(readOnly = true)
    public Integer getTotalReceiptCount(String email) {
        Member member = memberRepository.getMemberByEmail(email);
        return receiptRepository.countByMember(member);
    }

    @Transactional(readOnly = true)
    public ReceiptDetailResponseDto getReceiptInfo(String email, Long receiptId) {
        Member member = memberRepository.getMemberByEmail(email);
        Receipt receipt = receiptRepository.getReceiptById(receiptId);
        if (!receipt.getMember().equals(member)) {
            throw new CustomException(ErrorStatus.RECEIPT_NOT_AUTHORIZED,
                ErrorStatus.RECEIPT_NOT_AUTHORIZED.getMessage());
        }
        return ReceiptDetailResponseDto.convertToDto(receipt);
    }

    @Transactional
    public ReceiptDetailResponseDto uploadReceipt(String email, MultipartFile file, Long groupId) {
        Member member = memberRepository.getMemberByEmail(email);
        Group group = groupRepository.getGroupById(groupId);
        try {
            Receipt receipt = uploadToBucket(member, file, group);
            ReceiptInfo receiptInfo = geminiUtil.sendReceipt(file);
            receipt.updateReceipt(receiptInfo);
            return ReceiptDetailResponseDto.convertToDto(receipt);
        } catch (Exception e) {
            throw new CustomException(ErrorStatus.GOOGLE_VISION_API_ERROR, ErrorStatus.GOOGLE_VISION_API_ERROR.getMessage());
        }
    }

    /**
     * Google Cloud Storage 에 이미지 업로드 후 이미지 URI 반환
     */
    private Receipt uploadToBucket(Member member, MultipartFile file, Group group) throws IOException {
        String imageUri = IMAGE_URI_PREFIX + dataBucketUtil.uploadImage(file);
        Receipt receipt = Receipt.builder()
            .member(member)
            .imageUri(imageUri)
            .group(group)
            .build();
        return receiptRepository.save(receipt);
    }

    @Transactional
    public void updateReceipt(String email, Long receiptId, ReceiptUpdateRequestDto receiptUpdateRequestDto) {
        Member member = memberRepository.getMemberByEmail(email);
        Receipt receipt = receiptRepository.getReceiptById(receiptId);
        if (!receipt.getMember().equals(member)) {
            throw new CustomException(ErrorStatus.RECEIPT_NOT_AUTHORIZED, ErrorStatus.RECEIPT_NOT_AUTHORIZED.getMessage());
        }
        receipt.updateReceipt(receiptUpdateRequestDto);
        receiptRepository.save(receipt);
    }
}
