package com.smr.savemyreceipt_v2.oauth2;

import com.smr.savemyreceipt_v2.domain.Member;
import com.smr.savemyreceipt_v2.infrastructure.MemberRepository;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                .getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());

        String email = oAuth2User.getAttribute("email");

        Member member = getMember(attributes, email);

        return new CustomOAuth2User(
            Collections.singleton(new SimpleGrantedAuthority(member.getAuthority().name())),
            oAuth2User.getAttributes(),
            attributes.getNameAttributeKey(),
            member.getAuthority(),
            attributes.getOAuth2UserInfo().getId()
        );
    }

    private Member getMember(OAuthAttributes attributes, String email) {
        Member member = memberRepository.findByOauth2Id(attributes.getOAuth2UserInfo().getId()).orElse(null);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            return saveMember(attributes, email);
        }

        if (member == null) {
            return saveMember(attributes);
        }
        return member;
    }

    private Member saveMember(OAuthAttributes attributes) {
        Member member = attributes.toEntity(attributes.getOAuth2UserInfo());
        return memberRepository.save(member);
    }

    private Member saveMember(OAuthAttributes attributes, String email) {
        Member member = memberRepository.getMemberByEmail(email);
        member.changeOauth2Id(attributes.getOAuth2UserInfo().getId());
        return memberRepository.save(member);
    }
}
