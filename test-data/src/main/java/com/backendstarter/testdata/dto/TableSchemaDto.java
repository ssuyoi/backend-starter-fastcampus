package com.backendstarter.testdata.dto;

import com.backendstarter.testdata.domain.TableSchema;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public record TableSchemaDto(
    Long id,
    String schemaName,
    String userId,
    LocalDateTime exportedAt,
    Set<SchemaFieldDto> schemaFields,

    LocalDateTime createdAt,
    String createdBy,
    LocalDateTime updatedAt,
    String updatedBy
) {

    public static TableSchemaDto of(
        Long id,
        String schemaName,
        String userId,
        LocalDateTime exportedAt,
        Set<SchemaFieldDto> schemaFields,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime updatedAt,
        String updatedBy) {
        return new TableSchemaDto(
            id,
            schemaName,
            userId,
            exportedAt,
            schemaFields,
            createdAt,
            createdBy,
            updatedAt,
            updatedBy
        );
    }

    public static TableSchemaDto of(String schemaName, String userId, LocalDateTime exportedAt, Set<SchemaFieldDto> schemaFields) {
        return new TableSchemaDto(null, schemaName, userId, exportedAt, schemaFields, null, null, null, null);
    }

    /**
     * Entity -> DTO
     */
    public static TableSchemaDto fromEntity(TableSchema entity) {
        return new TableSchemaDto(
            entity.getId(),
            entity.getSchemaName(),
            entity.getUserId(),
            entity.getExportedAt(),
            entity.getSchemaFields().stream()
                .map(SchemaFieldDto::fromEntity)
                .collect(Collectors.toUnmodifiableSet()),
            entity.getCreatedAt(),
            entity.getCreatedBy(),
            entity.getUpdatedAt(),
            entity.getUpdatedBy()
        );
    }

    /**
     * DTO -> Entity
     */
    public TableSchema createEntity() {
        TableSchema entity =  TableSchema.of(this.schemaName(), this.userId());
        entity.addSchemaFields(schemaFields.stream().map(SchemaFieldDto::createEntity).toList());

        return entity;
    }

    /**
     * DTO -> Entity
     * 필수값에 null이 들어오는 경우 수정하지 않는다.
     */
    public TableSchema updateEntity(TableSchema entity) {
        if (schemaName != null) { entity.setSchemaName(schemaName);}
        if (userId != null) { entity.setUserId(userId);}
        entity.setExportedAt(exportedAt);
        if (schemaFields != null) {
            entity.clearSchemaFields(); // 기존 fields drop
            entity.addSchemaFields(
                schemaFields.stream().map(SchemaFieldDto::createEntity).toList()
            ); // 새로 채워주기
        }
        return entity;
    }
}