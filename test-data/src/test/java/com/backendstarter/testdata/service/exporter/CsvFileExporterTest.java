package com.backendstarter.testdata.service.exporter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.backendstarter.testdata.domain.constant.ExportFileType;
import com.backendstarter.testdata.domain.constant.MockDataType;
import com.backendstarter.testdata.dto.SchemaFieldDto;
import com.backendstarter.testdata.dto.TableSchemaDto;
import java.util.Set;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("[Logic] CSV 파일 출력기 테스트")
class CsvFileExporterTest {

    private CsvFileExporter sut = new CsvFileExporter();

    @DisplayName("테이블 스키마 정보와 행 수가 주어지면 CSV 형식의 문자열을 생성한다.")
    @Test
    void givenSchemaAndRowCount_whenExporting_thenReturnsCSVFormattedString() {

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
            )
        );

        int rowCount = 10;

        // when
        String result = sut.export(dto, rowCount);

        // then
        System.out.println(result); // 관찰용
        assertThat(result).startsWith("id,name,age,car,created_at");

    }

}