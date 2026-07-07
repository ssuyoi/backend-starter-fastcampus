package com.backendstarter.testdata.dto.request;

import com.backendstarter.testdata.dto.TableSchemaDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Data
public class TableSchemaRequest {

    private String schemaName;
    private String userId;
    private List<SchemaFieldRequest> schemaFields;

    public TableSchemaDto toDto() {
        return TableSchemaDto.of(schemaName, userId, null,
            schemaFields.stream().map(SchemaFieldRequest::toDto).collect(
                Collectors.toUnmodifiableSet()));
    }

}
