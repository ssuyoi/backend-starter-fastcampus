package com.backendstarter.testdata.repository;

import com.backendstarter.testdata.domain.TableSchema;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableSchemaRepository extends JpaRepository<TableSchema, Long> {
    Page<TableSchema> findByUserId(String userId, Pageable pageable);
    Optional<TableSchema> findBySchemaNameAndUserId( String schemaName, String userId);
    void deleteBySchemaNameAndUserId(String schemaName, String userId);
}
