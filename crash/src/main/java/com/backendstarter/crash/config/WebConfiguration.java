package com.backendstarter.crash.config;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class WebConfiguration {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;


    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000", "http://127.0.0.1:3000"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PATCH", "DELETE"));
        configuration.setAllowedHeaders(List.of("*")); // header를 통해 accessToken 값 전달 받음
        // 만든 configuration이 특정 url에서만 적용될 수 있도록 설정
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/v1/**", configuration);
        return source;
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, JwtExceptionFilter jwtExceptionFilter) throws Exception {
        // cors 설정 별도로 덮어쓰기 위해 기본 설정으로 생성
        http.cors(Customizer.withDefaults())
            .authorizeHttpRequests(
                (requests) ->
                    requests
                        // 정해진 url만 허용
                        .requestMatchers(HttpMethod.POST, "/api/*/users", "/api/*/users/authenticate")
                        .permitAll()
                        // 나머지에 대해서는 인증 확인
                        .anyRequest()
                        .authenticated())
            // session stateless
            .sessionManagement(
                (session) ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // csrf 비활성화
            .csrf(CsrfConfigurer::disable)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(jwtExceptionFilter, jwtAuthenticationFilter.getClass())
            .httpBasic(HttpBasicConfigurer::disable);

        return http.build();
    }
}
