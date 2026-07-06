package com.backendstarter.testdata.dto;

import com.backendstarter.testdata.domain.MockData;
import com.backendstarter.testdata.domain.constant.MockDataType;

public record MockDataDto(
    Long id,
    MockDataType mockDataType,
    String mockDataValue
) {

    public static MockDataDto fromEntity(MockData entity) {
        return new MockDataDto(entity.getId(), entity.getMockDataType(), entity.getMockDataValue());
    }
}
