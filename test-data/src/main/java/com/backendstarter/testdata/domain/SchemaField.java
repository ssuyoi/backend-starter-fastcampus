package com.backendstarter.testdata.domain;

import com.backendstarter.testdata.domain.constant.MockDataType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 특정 {@link TableSchema}의 단위 필드 정보 이 필드들이 모여서 테이블 스키마를 구성한다.
 *
 * @author sua
 */
@Getter
@ToString(callSuper = true)
@Entity
public class SchemaField extends AuditingFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @ManyToOne(optional = false)

    private TableSchema tableSchema;


    @Setter
    private @Column(nullable = false) String fieldName;
    @Setter
    private @Column(nullable = false) MockDataType mockDataType;
    @Setter
    private @Column(nullable = false) Integer fieldOrder;
    @Setter
    private @Column(nullable = false) Integer blankPercent;

    private String typeOptionJson; // man, max 등
    private String forceValue;

    protected SchemaField() {
    }

    public SchemaField(String fieldName, MockDataType mockDataType, Integer fieldOrder,
        Integer blankPercent, String typeOptionJson, String forceValue) {
        this.fieldName = fieldName;
        this.mockDataType = mockDataType;
        this.fieldOrder = fieldOrder;
        this.blankPercent = blankPercent;
        this.typeOptionJson = typeOptionJson;
        this.forceValue = forceValue;
    }

    public static SchemaField of(String fieldName, MockDataType mockDataType, Integer fieldOrder,
        Integer blankPercent, String typeOptionJson, String forceValue) {
        return new SchemaField(fieldName, mockDataType, fieldOrder, blankPercent, typeOptionJson,
            forceValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SchemaField that)) {
            return false;
        }

        if (this.getId() == null) {
            return Objects.equals(getTableSchema().getId(), that.getTableSchema().getId())
                && Objects.equals(getMockDataType(), that.getMockDataType())
                && Objects.equals(getFieldName(), that.getFieldName())
                && Objects.equals(getFieldOrder(), that.getFieldOrder())
                && Objects.equals(getBlankPercent(), that.getBlankPercent())
                && Objects.equals(getTypeOptionJson(), that.getTypeOptionJson())
                && Objects.equals(getForceValue(), that.getForceValue());
        }

        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        if (getId() == null) {
            return Objects.hash(getTableSchema().getId(), getMockDataType(),
                getFieldName(), getFieldOrder(), getBlankPercent(),
                getTypeOptionJson(), getForceValue());
        }

        return Objects.hash(getId());
    }
}
