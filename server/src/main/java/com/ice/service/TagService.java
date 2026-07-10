package com.ice.service;

import com.ice.dto.tag.CreateTagRequest;
import com.ice.dto.tag.TagItemDto;
import com.ice.dto.tag.TagListResponse;
import com.ice.exception.ApiException;
import java.util.List;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ice.generated.jooq.tables.Tag.TAG;

@Service
public class TagService {

    private final DSLContext dsl;

    public TagService(DSLContext dsl) {
        this.dsl = dsl;
    }

    public TagListResponse search(String query) {
        String q = query == null ? "" : query.trim();
        var condition = q.isEmpty() ? DSL.noCondition() : TAG.NAME.like("%" + q + "%");
        List<TagItemDto> items = dsl.select(TAG.ID, TAG.NAME)
                .from(TAG)
                .where(condition)
                .orderBy(TAG.USE_COUNT.desc(), TAG.ID.asc())
                .limit(20)
                .fetch(record -> new TagItemDto(record.get(TAG.ID), record.get(TAG.NAME)));
        return new TagListResponse(items);
    }

    @Transactional
    public TagItemDto create(CreateTagRequest request) {
        if (request.name() == null || request.name().isBlank()) {
            throw new ApiException("VALIDATION_ERROR", "标签名不能为空", HttpStatus.BAD_REQUEST);
        }
        String name = request.name().trim();
        if (name.length() > 32) {
            throw new ApiException("VALIDATION_ERROR", "标签名过长", HttpStatus.BAD_REQUEST);
        }
        TagItemDto existing = dsl.select(TAG.ID, TAG.NAME)
                .from(TAG)
                .where(TAG.NAME.eq(name))
                .fetchOne(record -> new TagItemDto(record.get(TAG.ID), record.get(TAG.NAME)));
        if (existing != null) {
            return existing;
        }
        Long id = dsl.insertInto(TAG)
                .set(TAG.NAME, name)
                .returningResult(TAG.ID)
                .fetchOne(TAG.ID);
        return new TagItemDto(id, name);
    }
}
