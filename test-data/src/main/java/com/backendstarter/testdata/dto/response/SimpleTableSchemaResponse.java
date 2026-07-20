package com.backendstarter.testdata.dto.response;

import com.backendstarter.testdata.dto.TableSchemaDto;
import java.time.LocalDateTime;

public record SimpleTableSchemaResponse(
    String schemaName,
    String userId,
    LocalDateTime updatedAt
) {
    public static SimpleTableSchemaResponse fromDto(TableSchemaDto dto) {
        return new SimpleTableSchemaResponse(dto.schemaName(), dto.userId(), dto.updatedAt());
    }
}
