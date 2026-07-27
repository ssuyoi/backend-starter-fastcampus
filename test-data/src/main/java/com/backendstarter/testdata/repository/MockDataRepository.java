package com.backendstarter.testdata.repository;

import com.backendstarter.testdata.domain.MockData;
import com.backendstarter.testdata.domain.constant.MockDataType;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MockDataRepository extends JpaRepository<MockData, Long> {

    @Cacheable("mockData")
    List<MockData> findByMockDataType(MockDataType mockDataType);
}
