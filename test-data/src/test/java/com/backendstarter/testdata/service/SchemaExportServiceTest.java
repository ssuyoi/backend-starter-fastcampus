package com.backendstarter.testdata.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import com.backendstarter.testdata.domain.TableSchema;
import com.backendstarter.testdata.domain.constant.ExportFileType;
import com.backendstarter.testdata.dto.TableSchemaDto;
import com.backendstarter.testdata.repository.TableSchemaRepository;
import com.backendstarter.testdata.service.exporter.MockDataFileExporterContext;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("[Service] 스키마 파일 출력 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class SchemaExportServiceTest {

    @InjectMocks private SchemaExportService sut;

    @Mock
    private MockDataFileExporterContext mockDataFileExporterContext;
    @Mock
    private TableSchemaRepository tableSchemaRepository;

    @DisplayName("출력 파일 유형과 스키마 정보와 행 수가 주어지면, 엔티티 출력 여부를 마킹하고, 알맞은 파일 유형으로 가짜 데이터 파일을 반환한다")
    @Test
    void givenFileTypeAndSchemaAndRowCount_whenExporting_thenMarksEntityOutputAndReturnsMockDataFile() {
        // given
        ExportFileType exportFileType = ExportFileType.CSV;
        TableSchemaDto dto = TableSchemaDto.of(
            "test_schema",
            "ssuyoi",
            null,
            null
        );
        int rowCount = 10;
        TableSchema exectedTableSchema = TableSchema.of(dto.schemaName(), dto.userId());
        given(tableSchemaRepository.findBySchemaNameAndUserId(dto.schemaName(), dto.userId()))
            .willReturn(Optional.of(exectedTableSchema));
        given(mockDataFileExporterContext.export(exportFileType, dto, rowCount)).willReturn("test,file,format");

        // when
        String result = sut.export(exportFileType, dto, rowCount);

        // then
        assertThat(result).isEqualTo("test,file,format");
        assertThat(exectedTableSchema.isExported()).isTrue();
        then(tableSchemaRepository).should().findBySchemaNameAndUserId(dto.schemaName(), dto.userId());
        then(mockDataFileExporterContext).should().export(exportFileType, dto, rowCount);
    }

    @DisplayName("입력 파라미터 중에 유저 식별 정보가 없으면, 스키마 테이블 조회를 시도하지 않는다")
    @Test
    void givenNoUserIdInParams_whenExporting_thenDoseNotTryToFindTableSchema() {
        // given
        ExportFileType exportFileType = ExportFileType.CSV;
        String userId = null;
        TableSchemaDto dto = TableSchemaDto.of(
            "test_schema",
            userId,
            null,
            null
        );
        int rowCount = 10;
        given(mockDataFileExporterContext.export(exportFileType, dto, rowCount)).willReturn("test,file,format");

        // when
        String result = sut.export(exportFileType, dto, rowCount);

        // then
        assertThat(result).isEqualTo("test,file,format");
        then(tableSchemaRepository).shouldHaveNoInteractions();
        then(mockDataFileExporterContext).should().export(exportFileType, dto, rowCount);
    }
}