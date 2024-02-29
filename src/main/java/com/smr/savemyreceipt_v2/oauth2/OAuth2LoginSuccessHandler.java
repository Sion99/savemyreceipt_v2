package com.smr.savemyreceipt_v2.oauth2;

import com.smr.savemyreceipt_v2.DTO.TokenDto;
import com.smr.savemyreceipt_v2.enums.Authority;
import com.smr.savemyreceipt_v2.jwt.TokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final TokenProvider tokenProvider;
    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication) throws IOException, ServletException {

        CustomOAuth2User customOAuth2User = (CustomOAuth2User) authentication.getPrincipal();
        String id = customOAuth2User.getOauth2Id();

        if (customOAuth2User.getAuthority() == Authority.ROLE_GUEST) {
            String accessToken = tokenProvider.generateTokenDto(authentication).getAccessToken();
            response.sendRedirect("http://localhost:3000/socialLoginNameInput" + "?token=" + accessToken + "&id=" + id);
            return;
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);
        redisTemplate.opsForValue().set("RT:" + authentication.getName(),
            tokenDto.getRefreshToken(),
            tokenProvider.getRefreshTokenExpireTime(),
            TimeUnit.MILLISECONDS);

        response.sendRedirect("https://localhost:3000/login" + "?accessToken=" + tokenDto.getAccessToken() + "&refreshToken=" + tokenDto.getRefreshToken());
    }
}
