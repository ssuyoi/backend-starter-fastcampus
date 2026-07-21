package com.backendstarter.testdata.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import com.backendstarter.testdata.dto.security.GithubUser;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    PathRequest.toStaticResources().atCommonLocations() // 정적 리소스
                ).permitAll()
                .requestMatchers(
                    HttpMethod.GET,           // GET 메서드만
                    "/",                    // 메인 화면
                    "table-schema",         // 테이블 스키마 만들기
                    "table-schema/export"   // 테이블 스키마 내보내기
                ).permitAll()
                .anyRequest().authenticated())  // 나머지는 모두 인증 요구
            .oauth2Login(withDefaults())        // OAuth2 로그인
            .logout(logout -> logout.logoutSuccessUrl("/"))
            .build();
    }

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService() {
        final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();


        return userRequest -> GithubUser.from(
            // provider 엔드포인트로 HTTP 요청을 보내서 프로필을 받아옴(loadUser)
            // -> 이 응답을 Map<String, Object> 형태로 꺼냄 (getAttributes)
            delegate.loadUser(userRequest).getAttributes()
        );
    }
}
