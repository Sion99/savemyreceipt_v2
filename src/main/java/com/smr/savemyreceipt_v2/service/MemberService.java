package com.smr.savemyreceipt_v2.service;

import com.smr.savemyreceipt_v2.DTO.member.request.MemberUpdateRequestDto;
import com.smr.savemyreceipt_v2.DTO.member.response.MemberDetailResponseDto;
import com.smr.savemyreceipt_v2.domain.Member;
import com.smr.savemyreceipt_v2.exception.ErrorStatus;
import com.smr.savemyreceipt_v2.exception.model.NotFoundException;
import com.smr.savemyreceipt_v2.infrastructure.MemberRepository;
import com.smr.savemyreceipt_v2.utils.DataBucketUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final DataBucketUtil dataBucketUtil;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;
    private final static String IMAGE_URI_PREFIX = "https://storage.googleapis.com/";

    @Transactional
    public MemberDetailResponseDto getMemberInfo(String email) {
        Member member = memberRepository.getMemberByEmail(email);
        return MemberDetailResponseDto.convertToDto(member);
    }

    @Transactional
    public MemberDetailResponseDto updateMemberInfo(MemberUpdateRequestDto memberUpdateRequestDto, String email) {
        Member member = memberRepository.getMemberByEmail(email);
        member.updateName(memberUpdateRequestDto.getName());
        memberRepository.save(member);
        return MemberDetailResponseDto.convertToDto(member);
    }

    @Transactional
    public MemberDetailResponseDto updateProfileImage(MultipartFile file, String email) {
        Member member = memberRepository.getMemberByEmail(email);
        if (file.getContentType() == null || !file.getContentType().startsWith("image")) {
            throw new NotFoundException(ErrorStatus.INVALID_FILE_TYPE,
                ErrorStatus.INVALID_FILE_TYPE.getMessage());
        }

        // Google Cloud Storage에 이미지 업로드
        String imageUri;
        try {
            imageUri = IMAGE_URI_PREFIX + dataBucketUtil.uploadImage(file);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NotFoundException(ErrorStatus.GOOGLE_CLOUD_STORAGE_ERROR,
                ErrorStatus.GOOGLE_CLOUD_STORAGE_ERROR.getMessage());
        }

        // 기존의 profile 사진이 버킷에 업로드 되어있는 경우 지워주기
        if (member.getProfileUri().startsWith(IMAGE_URI_PREFIX)) {
            String uuid = member.getProfileUri()
                .substring(IMAGE_URI_PREFIX.length() + bucketName.length() + 1);
            log.info("uuid: {}", uuid);
            dataBucketUtil.deleteImage(uuid);
        }

        member.updateProfileUri(imageUri);
        memberRepository.save(member);

        return MemberDetailResponseDto.convertToDto(member);
    }
}
