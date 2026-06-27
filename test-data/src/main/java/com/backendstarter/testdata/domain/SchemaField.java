package com.backendstarter.testdata.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SchemaField {

    private String fieldName;
    private String mockData;
    private Integer fieldOrder;
    private Integer blankPercent;
    private String typeOptionJson; // man, max 등
    private String forceValue;
}
