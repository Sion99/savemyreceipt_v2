package com.smr.savemyreceipt_v2.oauth2;

import com.smr.savemyreceipt_v2.domain.Member;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;

@Getter
public class OAuthAttributes {

    private final String nameAttributeKey;
    private final OAuth2UserInfo oAuth2UserInfo;

    @Builder
    public OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oAuth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
            .nameAttributeKey(userNameAttributeName)
            .oAuth2UserInfo(new OAuth2UserInfo(attributes))
            .build();
    }

    public Member toEntity(OAuth2UserInfo oAuth2UserInfo) {
        return Member.builder()
            .name(oAuth2UserInfo.getName())
            .email(oAuth2UserInfo.getEmail())
            .profileUri(oAuth2UserInfo.getPicture())
            .oauth2Id(oAuth2UserInfo.getId())
            .build();
    }
}
