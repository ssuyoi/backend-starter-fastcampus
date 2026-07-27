package com.backendstarter.testdata.service;

import com.backendstarter.testdata.domain.TableSchema;
import com.backendstarter.testdata.domain.constant.ExportFileType;
import com.backendstarter.testdata.dto.TableSchemaDto;
import com.backendstarter.testdata.repository.TableSchemaRepository;
import com.backendstarter.testdata.service.exporter.MockDataFileExporterContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@RequiredArgsConstructor
@Service
public class SchemaExportService {

    private final MockDataFileExporterContext mockDataFileExporterContext;
    private final TableSchemaRepository tableSchemaRepository;

    public String export(ExportFileType fileType, TableSchemaDto dto, Integer rowCount) {

        if (dto.userId() != null) {
            tableSchemaRepository.findBySchemaNameAndUserId(dto.schemaName(), dto.userId())
                .ifPresent(TableSchema::markExported);
        }
        return mockDataFileExporterContext.export(fileType, dto, rowCount);
    }

}
