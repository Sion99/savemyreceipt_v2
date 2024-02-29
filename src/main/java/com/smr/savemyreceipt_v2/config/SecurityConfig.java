package com.smr.savemyreceipt_v2.config;


import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.smr.savemyreceipt_v2.jwt.CustomAccessDeniedHandler;
import com.smr.savemyreceipt_v2.jwt.CustomEntryPoint;
import com.smr.savemyreceipt_v2.jwt.JwtFilter;
import com.smr.savemyreceipt_v2.jwt.TokenProvider;
import com.smr.savemyreceipt_v2.oauth2.OAuth2LoginFailureHandler;
import com.smr.savemyreceipt_v2.oauth2.OAuth2LoginSuccessHandler;
import com.smr.savemyreceipt_v2.oauth2.OAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomEntryPoint entryPoint;
    private final OAuth2UserService oAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public Storage storage() {
        return StorageOptions.getDefaultInstance().getService();
    }

    private static final String[] WHITE_LIST = {
        "/api/auth/**",
        "/swagger-ui/**",
        "/api-docs/**",
        "/login"
    };

    private static final String[] AUTHENTICATION_LIST = {
        "/api/v1/**"
    };

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .cors(AbstractHttpConfigurer::disable)
            .headers(c -> c.frameOptions(FrameOptionsConfig::disable).disable())

            .oauth2Login(oauth2Login -> oauth2Login
                .successHandler(oAuth2LoginSuccessHandler) // OAuth2 로그인 성공 핸들러
                .failureHandler(oAuth2LoginFailureHandler) // OAuth2 로그인 실패 핸들러
                .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                    .userService(oAuth2UserService) // 사용자 정보를 처리하는 서비스
                )
            )
            .authorizeHttpRequests(auth -> {
                auth
                    .requestMatchers(WHITE_LIST).permitAll()
                    .requestMatchers(AUTHENTICATION_LIST).hasRole("USER")
                    .anyRequest().authenticated();
            }).exceptionHandling(c ->
                c.authenticationEntryPoint(entryPoint).accessDeniedHandler(accessDeniedHandler)
            ).sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
