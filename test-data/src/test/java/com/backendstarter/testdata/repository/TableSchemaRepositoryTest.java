package com.backendstarter.testdata.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.backendstarter.testdata.domain.TableSchema;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

@DisplayName("[Repository] 테이블 스키마 쿼리 테스트")
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
class TableSchemaRepositoryTest {

    @Autowired private TableSchemaRepository tableSchemaRepository;

    @DisplayName("사용자별 테스트 스키마를 조회하면, 페이징된 테이블 스키마를 반환한다.")
    @Test
    void givenUserId_whenSelectingTableSchemas_thenReturnPagedTableSchemas() {
        // given
        var userId = "ssuyoi";

        // when
        Page<TableSchema> result = tableSchemaRepository.findByUserId(userId, Pageable.ofSize(5));

        // then
        assertThat(result.getContent())
            .hasSize(1)
            .extracting("userId", String.class)
            .containsOnly(userId);
        assertThat(result.getPageable())
            .hasFieldOrPropertyWithValue("pageSize", 5)
            .hasFieldOrPropertyWithValue("pageNumber", 0);

    }
    @DisplayName("사용자별 테스트 이름을 조회하면,테이블 스키마를 반환한다.")
    @Test
    void givenTestName_whenSelectingTableSchema_thenReturnTableSchema() {
        // given
        var userId = "ssuyoi";
        var schemaName = "test_schema1";

        // when
        Optional<TableSchema> result = tableSchemaRepository.findBySchemaNameAndUserId(schemaName, userId);

        // then
        assertThat(result)
            .get()
            .hasFieldOrPropertyWithValue("userId", userId)
            .hasFieldOrPropertyWithValue("schemaName", schemaName);

    }
    @DisplayName("사용자별 테스트 스키마 이름이 주어지면, 테이블 스키마를 삭제한다.")
    @Test
    void givenTestName_whenDeletingTableSchema_thenDeleteTableSchema() {
        // given
        var userId = "ssuyoi";
        var schemaName = "test_schema1";

        // when
        tableSchemaRepository.deleteBySchemaNameAndUserId(schemaName, userId);

        // then
        assertThat(tableSchemaRepository.findBySchemaNameAndUserId(schemaName, userId)).isEmpty();

    }
}