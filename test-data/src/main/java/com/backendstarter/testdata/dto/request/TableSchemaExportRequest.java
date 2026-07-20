package com.backendstarter.testdata.dto.request;

import com.backendstarter.testdata.domain.constant.ExportFileType;
import com.backendstarter.testdata.dto.TableSchemaDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Data
public class TableSchemaExportRequest {

    private String schemaName;
    private Integer rowCount;
    private ExportFileType fileType;
    private List<SchemaFieldRequest> schemaFields;

    public TableSchemaDto toDto(String userId) {
        return TableSchemaDto.of(schemaName, userId, null,
            schemaFields.stream().map(SchemaFieldRequest::toDto).collect(
                Collectors.toUnmodifiableSet()));
    }

}
