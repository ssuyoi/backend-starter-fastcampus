package com.backendstarter.testdata.service.exporter;

import com.backendstarter.testdata.domain.constant.ExportFileType;
import com.backendstarter.testdata.domain.constant.MockDataType;
import com.backendstarter.testdata.dto.SchemaFieldDto;
import com.backendstarter.testdata.dto.TableSchemaDto;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DisplayName("[IntegrationTest] 파일 출력기 컨텍스트 테스트")
@SpringBootTest
record MockDataFileExporterContextTest(@Autowired MockDataFileExporterContext sut) {

    @DisplayName("파일 형식과 테이블 스키마, 행 수가 주어지면, 파일 형식에 맞게 변환한 문자열을 리턴한다")
    @Test
    void givenFileTypeAndTableSchemaAndRowCount_whenExporting_thenReturnsFormattedString() {
        // given
        ExportFileType exportFileType = ExportFileType.CSV;
        TableSchemaDto dto = TableSchemaDto.of(
            "test_schema",
            "ssuyoi",
            null,
            Set.of(
                SchemaFieldDto.of("id", MockDataType.ROW_NUMBER, 1, 0, null, null),
                SchemaFieldDto.of("name", MockDataType.NAME, 2, 0, null, null),
                SchemaFieldDto.of("age", MockDataType.NUMBER, 3, 0, null, null),
                SchemaFieldDto.of("car", MockDataType.CAR, 4, 0, null, null),
                SchemaFieldDto.of("created_at", MockDataType.DATETIME, 5, 0, null, null)
            ));

        int rowCount = 10;

        // when
        String result = sut.export(exportFileType, dto, rowCount);

        // then
        System.out.println(result); // 관찰용

    }
}