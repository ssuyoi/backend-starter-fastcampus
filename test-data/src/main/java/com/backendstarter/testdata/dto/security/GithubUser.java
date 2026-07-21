package com.backendstarter.testdata.dto.security;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public record GithubUser(
    String id,
    String name,
    String email
) implements OAuth2User {

    // Map 형태로 받아온 사용자 데이터 중 필요한 값만 GithubUser로 매핑
    public static GithubUser from (Map<String, Object> attributes) {
        return new GithubUser(
            String.valueOf(attributes.get("login")),
            String.valueOf(attributes.get("name")), // nullable
            String.valueOf(attributes.get("email")) // nullable
        );
    }

    @Override
    public Map<String, Object> getAttributes() { return Map.of(); }

    // 로그인한 사용자에게 부여할 권한 목록 - 역할 구분이 없어 현재 빈 목록
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() { return List.of(); }

    // Spring Security가 사용자를 식별하는 Principal 이름
    @Override
    public String getName() {
        return name.equals("null") ? id : name;
    }

}
