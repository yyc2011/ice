package com.ice.service.admin;

import com.ice.dto.admin.AdminCategoryDto;
import com.ice.dto.admin.AdminCategoryRequest;
import com.ice.exception.ApiException;
import java.util.List;
import org.jooq.DSLContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ice.generated.jooq.tables.Article.ARTICLE;
import static com.ice.generated.jooq.tables.Category.CATEGORY;

@Service
public class AdminCategoryService {

    private final DSLContext dsl;

    public AdminCategoryService(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<AdminCategoryDto> list() {
        return dsl.select(CATEGORY.ID, CATEGORY.PARENT_ID, CATEGORY.NAME,
                        CATEGORY.IS_HOME_RECOMMENDED, CATEGORY.HOME_SORT_ORDER)
                .from(CATEGORY)
                .orderBy(CATEGORY.PARENT_ID.asc(), CATEGORY.SORT_ORDER.asc())
                .fetch(record -> new AdminCategoryDto(
                        record.get(CATEGORY.ID),
                        record.get(CATEGORY.PARENT_ID),
                        record.get(CATEGORY.NAME),
                        record.get(CATEGORY.IS_HOME_RECOMMENDED) != null && record.get(CATEGORY.IS_HOME_RECOMMENDED) == 1,
                        record.get(CATEGORY.HOME_SORT_ORDER) == null ? 0 : record.get(CATEGORY.HOME_SORT_ORDER)
                ));
    }

    @Transactional
    public AdminCategoryDto create(AdminCategoryRequest request) {
        validate(request);
        Long id = dsl.insertInto(CATEGORY)
                .set(CATEGORY.PARENT_ID, request.parent_id() == null ? 0L : request.parent_id())
                .set(CATEGORY.NAME, request.name().trim())
                .set(CATEGORY.SORT_ORDER, request.sort_order() == null ? 0 : request.sort_order())
                .set(CATEGORY.IS_HOME_RECOMMENDED, (byte) (Boolean.TRUE.equals(request.is_home_recommended()) ? 1 : 0))
                .set(CATEGORY.HOME_SORT_ORDER, request.home_sort_order() == null ? 0 : request.home_sort_order())
                .returningResult(CATEGORY.ID)
                .fetchOne(CATEGORY.ID);
        return get(id);
    }

    @Transactional
    public AdminCategoryDto update(long id, AdminCategoryRequest request) {
        validate(request);
        int updated = dsl.update(CATEGORY)
                .set(CATEGORY.NAME, request.name().trim())
                .set(CATEGORY.SORT_ORDER, request.sort_order() == null ? 0 : request.sort_order())
                .set(CATEGORY.IS_HOME_RECOMMENDED, (byte) (Boolean.TRUE.equals(request.is_home_recommended()) ? 1 : 0))
                .set(CATEGORY.HOME_SORT_ORDER, request.home_sort_order() == null ? 0 : request.home_sort_order())
                .where(CATEGORY.ID.eq(id))
                .execute();
        if (updated == 0) {
            throw new ApiException("NOT_FOUND", "分类不存在", HttpStatus.NOT_FOUND);
        }
        return get(id);
    }

    @Transactional
    public void delete(long id) {
        var category = dsl.selectFrom(CATEGORY).where(CATEGORY.ID.eq(id)).fetchOne();
        if (category == null) {
            throw new ApiException("NOT_FOUND", "分类不存在", HttpStatus.NOT_FOUND);
        }
        if (category.getIsDefault() != null && category.getIsDefault() == 1) {
            throw new ApiException("FORBIDDEN", "默认分类不可删除", HttpStatus.FORBIDDEN);
        }
        int childCount = dsl.fetchCount(dsl.selectFrom(CATEGORY).where(CATEGORY.PARENT_ID.eq(id)));
        if (childCount > 0) {
            throw new ApiException("FORBIDDEN", "存在子分类，不可删除", HttpStatus.FORBIDDEN);
        }
        int articleCount = dsl.fetchCount(dsl.selectFrom(ARTICLE).where(ARTICLE.CATEGORY_ID.eq(id)));
        if (articleCount > 0) {
            throw new ApiException("FORBIDDEN", "仍有文章使用此分类", HttpStatus.FORBIDDEN);
        }
        dsl.deleteFrom(CATEGORY).where(CATEGORY.ID.eq(id)).execute();
    }

    private AdminCategoryDto get(long id) {
        return dsl.select(CATEGORY.ID, CATEGORY.PARENT_ID, CATEGORY.NAME,
                        CATEGORY.IS_HOME_RECOMMENDED, CATEGORY.HOME_SORT_ORDER)
                .from(CATEGORY)
                .where(CATEGORY.ID.eq(id))
                .fetchOne(record -> new AdminCategoryDto(
                        record.get(CATEGORY.ID),
                        record.get(CATEGORY.PARENT_ID),
                        record.get(CATEGORY.NAME),
                        record.get(CATEGORY.IS_HOME_RECOMMENDED) != null && record.get(CATEGORY.IS_HOME_RECOMMENDED) == 1,
                        record.get(CATEGORY.HOME_SORT_ORDER) == null ? 0 : record.get(CATEGORY.HOME_SORT_ORDER)
                ));
    }

    private void validate(AdminCategoryRequest request) {
        if (request.name() == null || request.name().isBlank()) {
            throw new ApiException("VALIDATION_ERROR", "分类名不能为空", HttpStatus.BAD_REQUEST);
        }
    }
}
