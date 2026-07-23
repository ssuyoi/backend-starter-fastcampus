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
            .orElseThrow(() -> new EntityNotFoundException(
                "테이블 스키마가 없습니다 - userId = " + userId + " , schema = " + schemaName));
    }

    /**
     * 해당 스키마가 있는지 조회
     * -> 있으면 수정
     * -> 없으면 생성
     */
    public void upsertTableSchema(TableSchemaDto dto) {
        tableSchemaRepository.findBySchemaNameAndUserId(dto.schemaName(), dto.userId())
            .ifPresentOrElse(
                entity -> tableSchemaRepository.save(dto.updateEntity(entity)),
                    () -> tableSchemaRepository.save(dto.createEntity())
                ); // optional -> ifPresentOrElse

    }
}
