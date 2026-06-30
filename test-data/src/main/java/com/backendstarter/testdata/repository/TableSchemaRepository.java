package com.backendstarter.testdata.repository;

import com.backendstarter.testdata.domain.MockData;
import com.backendstarter.testdata.domain.TableSchema;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableSchemaRepository extends JpaRepository<TableSchema, Long> {
}
