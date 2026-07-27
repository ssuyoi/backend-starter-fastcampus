package com.backendstarter.testdata.controller;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.oauth2Login;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlTemplate;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import com.backendstarter.testdata.config.SecurityConfig;
import com.backendstarter.testdata.domain.constant.ExportFileType;
import com.backendstarter.testdata.domain.constant.MockDataType;
import com.backendstarter.testdata.dto.TableSchemaDto;
import com.backendstarter.testdata.dto.request.SchemaFieldRequest;
import com.backendstarter.testdata.dto.request.TableSchemaExportRequest;
import com.backendstarter.testdata.dto.request.TableSchemaRequest;
import com.backendstarter.testdata.dto.response.SimpleTableSchemaResponse;
import com.backendstarter.testdata.dto.security.GithubUser;
import com.backendstarter.testdata.service.SchemaExportService;
import com.backendstarter.testdata.service.TableSchemaService;
import com.backendstarter.testdata.util.FormDataEncoder;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

//@Disabled("강의 내용에서 테스트만 다루므로 테스트를 먼저 작성함. 테스트로 스펙을 전달하고, 아직 구현이 없으므로 비활성화")
@DisplayName("[Controller] 테이블 스키마 컨트롤러 테스트")
@Import({SecurityConfig.class, FormDataEncoder.class})
@WebMvcTest(TableSchemaController.class)
class TableSchemaControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private FormDataEncoder formDataEncoder;
    @Autowired
    private ObjectMapper mapper;

    @MockitoBean
    private TableSchemaService tableSchemaService;
    @MockitoBean
    private SchemaExportService schemaExportService;

    @DisplayName("[GET] 테이블 스키마 페이지 -> 비로그인 최초 진입 테이블 스키마 뷰 (정상)")
    @Test
    void givenNothing_whenRequesting_thenShowsTableSchemaView() throws Exception {
        // given

        // when
        // then
        mvc.perform(get("/table-schema"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(model().attributeExists("tableSchema"))
            .andExpect(model().attributeExists("mockDataTypes"))
            .andExpect(model().attributeExists("fileTypes"))
            .andExpect(view().name("table-schema"));
        then(tableSchemaService).shouldHaveNoInteractions();
    }

    @DisplayName("[GET] 테이블 스키마 조회, 로그인 + 특정 테이블 스키마 (정상)")
    @Test
    void givenAuthenticatedUserAndSchemaName_whenRequesting_thenShowsTableSchema()
        throws Exception {
        // given
        var githubUser = new GithubUser("test-id", "test-name", "test@email.com");
        var schemaName = "test_schema";
        given(tableSchemaService.loadMySchema(githubUser.id(), schemaName)).willReturn(
            TableSchemaDto.of(schemaName, githubUser.id(), null, Set.of()));

        // when
        // then
        mvc.perform(get("/table-schema")
                .queryParam("schemaName", schemaName)
                .with(oauth2Login().oauth2User(githubUser)))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(model().attributeExists("tableSchema"))
            //.andExpect(model().attribute("tableSchema", hasProperty("schemaName", is(schemaName))))
            .andExpect(model().attributeExists("mockDataTypes"))
            .andExpect(model().attributeExists("fileTypes"))
            .andExpect(content().string(containsString(schemaName)))
            .andExpect(view().name("table-schema"));
        then(tableSchemaService).should().loadMySchema(githubUser.id(), schemaName);
    }


    @DisplayName("[POST] 테이블 스키마 생성 및 변경 (정상)")
    @Test
    void givenTableSchemaRequest_whenUpsert_thenRedirectsToTableSchemaView() throws Exception {
        // given
        var githubUser = new GithubUser("test-id", "test-name", "test@email.com");
        TableSchemaRequest request = TableSchemaRequest.of(
            "test_schema",
            List.of(
                SchemaFieldRequest.of("id", MockDataType.ROW_NUMBER, 1, 0, null, null),
                SchemaFieldRequest.of("name", MockDataType.NAME, 2, 10, null, null),
                SchemaFieldRequest.of("age", MockDataType.NUMBER, 3, 10, null, null)
            )
        );
        willDoNothing().given(tableSchemaService).upsertTableSchema(request.toDto(githubUser.id()));
        // when
        // then
        mvc.perform(
                post("/table-schema")
                    .content(formDataEncoder.encode(request))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .with(csrf())
                    .with(oauth2Login().oauth2User(githubUser))
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlTemplate("/table-schema?schemaName={schemaName}", request.getSchemaName()));
        then(tableSchemaService).should().upsertTableSchema(request.toDto(githubUser.id()));
    }

    @DisplayName("[GET] 내 스키마 목록 페이지 -> 내 스키마 목록 뷰 (정상)")
    @Test
    void givenAuthenticatedUser_whenRequesting_thenShowsMySchemaView() throws Exception {
        // given
        var githubUser = new GithubUser("test-id", "test-name", "test@email.com");
        List<TableSchemaDto> expectedTableSchemas = List.of();
        given(tableSchemaService.loadMySchemas(githubUser.id())).willReturn(expectedTableSchemas);
        // when
        // then
        mvc.perform(
                get("/table-schema/my-schemas")
                    .with(oauth2Login().oauth2User(githubUser)))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_HTML))
            .andExpect(model().attribute("tableSchemas", expectedTableSchemas.stream().map(
                SimpleTableSchemaResponse::fromDto).toList()))
            .andExpect(view().name("my-schemas"));
        then(tableSchemaService).should().loadMySchemas(githubUser.id());


    }

    @DisplayName("[GET] 내 스키마 목록 조회 (비로그인)")
    @Test
    void givenNothing_whenRequesting_thenRedirectsToLogin() throws Exception {
        // given

        // when
        // then
        mvc.perform(get("/table-schema/my-schemas"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrlPattern("**/oauth2/authorization/github"));
        then(tableSchemaService).shouldHaveNoInteractions();

    }

    @DisplayName("[POST] 내 스키마 삭제 (정상)")
    @Test
    void givenAuthenticatedUserAndSchemaName_whenDeleting_thenRedirectsToTableSchemaView()
        throws Exception {
        // given
        var githubUser = new GithubUser("test-id", "test-name", "test@email.com");
        String schemaName = "test_schema";
        willDoNothing().given(tableSchemaService).deleteTableSchema(githubUser.id(), schemaName);

        // when
        // then
        mvc.perform(
                post("/table-schema/my-schemas/{schemaName}", schemaName)
                    .with(csrf())
                    .with(oauth2Login().oauth2User(githubUser))
            )
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/table-schema/my-schemas"));
        then(tableSchemaService).should().deleteTableSchema(githubUser.id(), schemaName);
    }

    @DisplayName("[GET] 테이블 스키마 파일 다운로드 -> 테이블 스키마 파일, 비로그인 (정상)")
    @Test
    void givenTableSchema_whenDownloading_thenReturnFile() throws Exception {
        // given
        TableSchemaExportRequest request = TableSchemaExportRequest.of(
            "test",
            77,
            ExportFileType.CSV,
            List.of(
                SchemaFieldRequest.of("id", MockDataType.ROW_NUMBER, 1, 0, null, null),
                SchemaFieldRequest.of("name", MockDataType.STRING, 1, 0, "option", "well"),
                SchemaFieldRequest.of("age", MockDataType.NUMBER, 3, 20, null, null)
            )
        );

        String queryParam = formDataEncoder.encode(request, false);
        String expectedBody = "id,name,age\n1,option,20";
        given(schemaExportService.export(request.getFileType(), request.toDto(null), request.getRowCount()))
            .willReturn(expectedBody);

        // when
        // then
        mvc.perform(get("/table-schema/export?" + queryParam))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
            .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=test.csv"))
            .andExpect(content().string(expectedBody));
        then(schemaExportService).should().export(request.getFileType(), request.toDto(null), request.getRowCount());

    }

    @DisplayName("[GET] 테이블 스키마 파일 다운로드 -> 테이블 스키마 파일, 로그인 (정상)")
    @Test
    void givenAuthenticatedUserAndTableSchema_whenDownloading_thenReturnFile() throws Exception {
        // given
        var githubUser = new GithubUser("test-id", "test-name", "test@email.com");

        TableSchemaExportRequest request = TableSchemaExportRequest.of(
            "test",
            77,
            ExportFileType.CSV,
            List.of(
                SchemaFieldRequest.of("id", MockDataType.ROW_NUMBER, 1, 0, null, null),
                SchemaFieldRequest.of("name", MockDataType.STRING, 1, 0, "option", "well"),
                SchemaFieldRequest.of("age", MockDataType.NUMBER, 3, 20, null, null)
            )
        );

        String queryParam = formDataEncoder.encode(request, false);
        String expectedBody = "id,name,age\n1,option,20";
        given(schemaExportService.export(request.getFileType(), request.toDto(githubUser.id()), request.getRowCount()))
            .willReturn(expectedBody);

        // when
        // then
        mvc.perform(get("/table-schema/export?" + queryParam)
                .with(oauth2Login().oauth2User(githubUser)))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
            .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=test.csv"))
            .andExpect(content().string(expectedBody));
        then(schemaExportService).should().export(request.getFileType(), request.toDto(githubUser.id()), request.getRowCount());

    }

}
