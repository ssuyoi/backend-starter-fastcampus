package com.backendstarter.testdata.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.backendstarter.testdata.config.SecurityConfig;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@Disabled("강의 내용에서 테스트만 다루므로 테스트를 먼저 작성함. 테스트로 스펙을 전달하고, 아직 구현이 없으므로 비활성화")
@DisplayName("[Controller] 회원 컨트롤러 테스트")
@Import(SecurityConfig.class)
@WebMvcTest
public record UserAccountControllerTest(
        @Autowired MockMvc mvc
) {
    @WithMockUser
    @DisplayName("[GET] 내 정보 페이지 -> 내 정보 뷰 (정상)")
    @Test
    void givenAuthenticatedUser_whenRequesting_thenShowsMyAccountView() throws Exception {
        // given


        // when
        // then
        mvc.perform(get("/my-account"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
            .andExpect(view().name("my-account"));

    }
}
