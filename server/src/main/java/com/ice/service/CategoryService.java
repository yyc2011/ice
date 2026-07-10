package com.ice.service;

import com.ice.dto.category.CategoryPreviewArticleDto;
import com.ice.dto.category.CategoryPreviewItemDto;
import com.ice.dto.category.CategoryPreviewResponse;
import com.ice.dto.category.CategoryNodeDto;
import com.ice.dto.category.CategoryTreeResponse;
import com.ice.domain.ArticleStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import static com.ice.generated.jooq.tables.Article.ARTICLE;
import static com.ice.generated.jooq.tables.Category.CATEGORY;

@Service
public class CategoryService {

    private final DSLContext dsl;
    private final RankingService rankingService;
    private final RankingConfigService rankingConfigService;

    public CategoryService(
            DSLContext dsl,
            RankingService rankingService,
            RankingConfigService rankingConfigService
    ) {
        this.dsl = dsl;
        this.rankingService = rankingService;
        this.rankingConfigService = rankingConfigService;
    }

    public CategoryTreeResponse getTree() {
        var records = dsl.select(CATEGORY.ID, CATEGORY.PARENT_ID, CATEGORY.NAME, CATEGORY.SORT_ORDER)
                .from(CATEGORY)
                .orderBy(CATEGORY.SORT_ORDER.asc(), CATEGORY.ID.asc())
                .fetch();

        Map<Long, List<CategoryNodeDto>> childrenByParent = new HashMap<>();
        for (var record : records) {
            long parentId = record.get(CATEGORY.PARENT_ID);
            CategoryNodeDto node = new CategoryNodeDto(
                    record.get(CATEGORY.ID),
                    record.get(CATEGORY.NAME),
                    new ArrayList<>()
            );
            childrenByParent.computeIfAbsent(parentId, key -> new ArrayList<>()).add(node);
        }

        attachChildren(childrenByParent, 0L);
        return new CategoryTreeResponse(childrenByParent.getOrDefault(0L, List.of()));
    }

    public CategoryPreviewResponse getPreview() {
        int hotDays = rankingConfigService.getInt("category_hot_days", 30);
        LocalDateTime since = LocalDateTime.now().minusDays(hotDays);

        var parents = dsl.select(CATEGORY.ID, CATEGORY.NAME)
                .from(CATEGORY)
                .where(CATEGORY.PARENT_ID.eq(0L))
                .and(CATEGORY.IS_HOME_RECOMMENDED.eq((byte) 1))
                .orderBy(CATEGORY.HOME_SORT_ORDER.asc(), CATEGORY.ID.asc())
                .limit(5)
                .fetch();

        List<CategoryPreviewItemDto> categories = new ArrayList<>();
        for (var parent : parents) {
            long parentId = parent.get(CATEGORY.ID);
            List<Long> childIds = dsl.select(CATEGORY.ID)
                    .from(CATEGORY)
                    .where(CATEGORY.PARENT_ID.eq(parentId))
                    .fetch(CATEGORY.ID);
            List<Long> categoryIds = new ArrayList<>(childIds);
            categoryIds.add(parentId);

            var articles = dsl.select(
                            ARTICLE.ID,
                            ARTICLE.TITLE,
                            ARTICLE.COVER_URL,
                            ARTICLE.VIEW_COUNT,
                            ARTICLE.LIKE_COUNT,
                            ARTICLE.COMMENT_COUNT,
                            ARTICLE.DISLIKE_COUNT
                    )
                    .from(ARTICLE)
                    .where(ARTICLE.CATEGORY_ID.in(categoryIds))
                    .and(ARTICLE.STATUS.eq(ArticleStatus.PUBLISHED.code()))
                    .and(ARTICLE.PUBLISHED_AT.ge(since))
                    .fetch();

            List<ScoredPreview> scored = new ArrayList<>();
            for (var record : articles) {
                double hotScore = rankingService.calculateHotScore(
                        record.get(ARTICLE.VIEW_COUNT) == null ? 0 : record.get(ARTICLE.VIEW_COUNT),
                        record.get(ARTICLE.LIKE_COUNT) == null ? 0 : record.get(ARTICLE.LIKE_COUNT),
                        record.get(ARTICLE.COMMENT_COUNT) == null ? 0 : record.get(ARTICLE.COMMENT_COUNT),
                        record.get(ARTICLE.DISLIKE_COUNT) == null ? 0 : record.get(ARTICLE.DISLIKE_COUNT)
                );
                scored.add(new ScoredPreview(
                        record.get(ARTICLE.ID),
                        record.get(ARTICLE.TITLE),
                        record.get(ARTICLE.COVER_URL),
                        hotScore
                ));
            }
            scored.sort(Comparator.comparingDouble(ScoredPreview::hotScore).reversed());
            List<CategoryPreviewArticleDto> topArticles = scored.stream()
                    .limit(5)
                    .map(s -> new CategoryPreviewArticleDto(
                            s.id(),
                            s.title(),
                            s.coverUrl(),
                            Math.round(s.hotScore() * 10.0) / 10.0
                    ))
                    .toList();
            categories.add(new CategoryPreviewItemDto(
                    parentId,
                    parent.get(CATEGORY.NAME),
                    topArticles
            ));
        }
        return new CategoryPreviewResponse(categories);
    }

    private record ScoredPreview(long id, String title, String coverUrl, double hotScore) {
    }

    private void attachChildren(Map<Long, List<CategoryNodeDto>> childrenByParent, long parentId) {
        List<CategoryNodeDto> nodes = childrenByParent.get(parentId);
        if (nodes == null) {
            return;
        }
        for (CategoryNodeDto node : nodes) {
            List<CategoryNodeDto> children = childrenByParent.getOrDefault(node.id(), List.of());
            node.children().addAll(children);
            attachChildren(childrenByParent, node.id());
        }
    }
}
