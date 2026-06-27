package com.backendstarter.testdata.domain;

import java.time.LocalDateTime;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TableSchema {

    private String schemaName;
    private String userId;
    private LocalDateTime exportedAt;
}