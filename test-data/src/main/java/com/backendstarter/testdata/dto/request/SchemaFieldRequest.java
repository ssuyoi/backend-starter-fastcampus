package com.backendstarter.testdata.dto.request;

import com.backendstarter.testdata.domain.constant.MockDataType;
import com.backendstarter.testdata.dto.SchemaFieldDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
@Data
public class SchemaFieldRequest{

    private String fieldName;
    private MockDataType mockDataType;
    private Integer fieldOrder;
    private Integer blankPercent;
    private String typeOptionJson;
    private String forceValue;

    public SchemaFieldDto toDto() {
        return SchemaFieldDto.of(
            fieldName,
            mockDataType,
            fieldOrder,
            blankPercent,
            typeOptionJson,
            forceValue
        );
    }

}
