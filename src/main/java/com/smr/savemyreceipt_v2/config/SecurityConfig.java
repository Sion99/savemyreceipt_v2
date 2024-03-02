package com.smr.savemyreceipt_v2.config;


import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.smr.savemyreceipt_v2.jwt.JwtFilter;
import com.smr.savemyreceipt_v2.jwt.TokenProvider;
import com.smr.savemyreceipt_v2.oauth2.OAuth2LoginFailureHandler;
import com.smr.savemyreceipt_v2.oauth2.OAuth2LoginSuccessHandler;
import com.smr.savemyreceipt_v2.oauth2.OAuth2UserService;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.util.ResourceUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;
    private final OAuth2UserService oAuth2UserService;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;

    @Value("${spring.cloud.gcp.storage.credentials.location}")
    private String keyFileLocation;

    // Google Cloud Storage 연결할 때 credentials.json 파일을 사용하기 위해 Storage Bean 생성
    // 이 부분 안해주면 401 unauthorized 에러 발생
    @Bean
    public Storage storage() throws IOException {
        InputStream keyFile = ResourceUtils.getURL(keyFileLocation).openStream();
        return StorageOptions.newBuilder()
            .setCredentials(GoogleCredentials.fromStream(keyFile))
            .build()
            .getService();
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
            .cors(corsCustomizer -> corsCustomizer.configurationSource(corsConfigurationSource()))
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
            }).sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .addFilterBefore(new JwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*")); // 모든 출처 허용
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH")); // 허용할 HTTP 메소드
        configuration.setAllowedHeaders(List.of("*")); // 모든 헤더 허용
        configuration.setAllowCredentials(true); // 쿠키를 포함한 요청 허용
        configuration.setMaxAge(3600L); // pre-flight 요청의 결과를 캐시하는 시간(초 단위)

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 모든 경로에 CORS 설정 적용
        return source;
    }

}
