package com.backendstarter.testdata.dto.response;

import com.backendstarter.testdata.domain.constant.MockDataType;
import com.backendstarter.testdata.dto.SchemaFieldDto;

public record SchemaFieldResponse(
    MockDataType mockDataType,
    String fieldName,
    Integer fieldOrder,
    Integer blankPercent,
    String typeOptionJson,
    String forceValue) {

    public static SchemaFieldResponse fromDto(SchemaFieldDto dto) {
        return new SchemaFieldResponse(
            dto.mockDataType(),
            dto.fieldName(),
            dto.fieldOrder(),
            dto.blankPercent(),
            dto.typeOptionJson(),
            dto.forceValue()
        );
    }
}
