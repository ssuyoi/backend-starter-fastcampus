package com.backendstarter.testdata.domain.constant;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MockDataType {
    STRING(Set.of("minLength", "maxLength", "pattern"), null),
    NUMBER(Set.of("min", "max", "decimal"), null),
    BOOLEAN(Set.of(), null),
    DATETIME(Set.of("from", "to"), null),
    ENUM(Set.of("elements"), null),

    SENTENCE(Set.of("minSentences", "maxSentences"), STRING),
    PARAGRAPH(Set.of("minParagraphs", "maxParagraphs"), STRING),
    UUID(Set.of(), STRING),
    EMAIL(Set.of(), STRING),
    CAR(Set.of(), STRING),
    ROW_NUMBER(Set.of("start", "step"), STRING),
    NAME(Set.of(), STRING)
    ;

    private final Set<String> requiredOptions;
    private final MockDataType baseType;

    /**
     * static final로 생성해서 toObjects 메소드가 호출될 때마다 매핑 변환 작업을 하지 않도록 한다.
     * MockDataType은 변동성이 거의 없기 때문에 이 방식이 유용
     */
    private static final List<MockDataTypeObject> objects =  Arrays.stream(MockDataType.values())
        .map(MockDataType::toObject).toList();

    /**
     * 해당 데이터타입이 베이스 타입인지 확인
     */
    public boolean isBaseType() {
        return baseType == null;
    }

    public static List<MockDataTypeObject> toObjects() {
        return objects;
    }

    public MockDataTypeObject toObject() {
        return new MockDataTypeObject(
            this.name(),
            this.requiredOptions,
            this.baseType == null ? null : this.baseType.name()
        );
    }

    public record MockDataTypeObject(
        String name,
        Set<String> requiredOptions,
        String baseType
    ) {}
}
