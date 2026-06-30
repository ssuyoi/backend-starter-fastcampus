package com.backendstarter.testdata.domain;

import com.backendstarter.testdata.domain.constant.MockDataType;
import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 특정 가짜 데이터 자료형에 대응하는 가짜 데이터 알고리즘으로 생성하지 않는 {@link MockDataType}의 경우, 이 가짜 데이터를 랜덤으로 뽑아 출력
 *
 * @author sua
 */
@Getter
@ToString
@Table(uniqueConstraints = {
    @UniqueConstraint(columnNames = {"mockDataType", "mockDataValue"})
})
@Entity
public class MockData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @Column(nullable = false)
    private MockDataType mockDataType;
    @Setter
    @Column(nullable = false)
    private String mockDataValue;

    protected MockData() {
    }

    public MockData(MockDataType mockDataType, @Nullable String mockDataValue) {
        this.mockDataType = mockDataType;
        this.mockDataValue = mockDataValue;
    }

    public static MockData of(MockDataType mockDataType, @Nullable String mockDataValue) {
        return new MockData(mockDataType, mockDataValue);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MockData mockData)) {
            return false;
        }

        if (getId() == null) {
            return Objects.equals(getMockDataType(), mockData.getMockDataType())
                && Objects.equals(getMockDataValue(), mockData.getMockDataValue());
        }

        return Objects.equals(getId(), mockData.getId());
    }

    @Override
    public int hashCode() {

        if (getId() == null) {
            return Objects.hash(getMockDataType(), getMockDataValue());
        }
        return Objects.hash(getId());
    }
}
