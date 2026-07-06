package com.backendstarter.testdata.dto.request;

import com.backendstarter.testdata.domain.constant.MockDataType;
import com.backendstarter.testdata.dto.SchemaFieldDto;

public record SchemaFieldRequest(
    String fieldName,
    MockDataType mockDataType,
    Integer fieldOrder,
    Integer blankPercent,
    String typeOptionJson,
    String forceValue) {

    public static SchemaFieldRequest of(String fieldName, MockDataType mockDataType,
        Integer fieldOrder, Integer blankPercent, String typeOptionJson, String forceValue) {
        return new SchemaFieldRequest(fieldName, mockDataType, fieldOrder, blankPercent, typeOptionJson, forceValue);
    }

    public SchemaFieldDto toDto() {
        return SchemaFieldDto.of(
            fieldName(),
            mockDataType(),
            fieldOrder(),
            blankPercent(),
            typeOptionJson(),
            forceValue()
        );
    }

}
