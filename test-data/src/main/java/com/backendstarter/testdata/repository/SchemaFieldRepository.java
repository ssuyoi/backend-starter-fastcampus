package com.backendstarter.testdata.repository;

import com.backendstarter.testdata.domain.MockData;
import com.backendstarter.testdata.domain.SchemaField;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchemaFieldRepository extends JpaRepository<SchemaField, Long> {
}
