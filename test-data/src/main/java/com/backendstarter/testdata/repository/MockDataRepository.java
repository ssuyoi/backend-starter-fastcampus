package com.backendstarter.testdata.repository;

import com.backendstarter.testdata.domain.MockData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MockDataRepository extends JpaRepository<MockData, Long> {


}
