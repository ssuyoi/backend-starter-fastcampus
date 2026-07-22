package com.backendstarter.testdata.service;

import com.backendstarter.testdata.dto.TableSchemaDto;
import com.backendstarter.testdata.repository.TableSchemaRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class TableSchemaService {

    private final TableSchemaRepository tableSchemaRepository;

    @Transactional(readOnly = true)
    public List<TableSchemaDto> loadMySchemas(String userId) {

        return loadMySchemas(userId, Pageable.unpaged()).toList();
    }

    @Transactional(readOnly = true)
    public Page<TableSchemaDto> loadMySchemas(String userId, Pageable pageable) {
        return tableSchemaRepository.findByUserId(userId, pageable)
            .map(TableSchemaDto::fromEntity);
    }

    @Transactional(readOnly = true)
    public TableSchemaDto loadMySchema(String userId, String schemaName) {
        return tableSchemaRepository.findBySchemaNameAndUserId(schemaName, userId)
            .map(TableSchemaDto::fromEntity)
            .orElseThrow(() -> new EntityNotFoundException("테이블 스키마가 없습니다 - userId = " + userId + " , schema = " + schemaName));
    }

    public void saveMySchema(TableSchemaDto dto) {
        tableSchemaRepository.save(dto.createEntity());
    }
}
