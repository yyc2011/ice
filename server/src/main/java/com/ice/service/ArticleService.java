package com.ice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ice.client.AiAuditClient;
import com.ice.client.dto.AiAuditResponse;
import com.ice.domain.ArticleStatus;
import com.ice.domain.ContentType;
import com.ice.domain.ReviewStatus;
import com.ice.domain.ReviewType;
import com.ice.dto.article.ArticleDetailResponse;
import com.ice.dto.article.CreateDraftRequest;
import com.ice.dto.article.CreateDraftResponse;
import com.ice.dto.article.FeaturedArticleItemDto;
import com.ice.dto.article.FeaturedArticlesResponse;
import com.ice.dto.article.LatestReviewDto;
import com.ice.dto.article.PublishResponse;
import com.ice.dto.article.ReviewAppealRequest;
import com.ice.dto.article.ReviewAppealResponse;
import com.ice.dto.article.TagDto;
import com.ice.exception.ApiException;
import com.ice.generated.jooq.tables.records.ArticleRecord;
import com.ice.generated.jooq.tables.records.ContentReviewRecord;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.jooq.DSLContext;
import org.jooq.JSON;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ice.generated.jooq.tables.Article.ARTICLE;
import static com.ice.generated.jooq.tables.ArticleImage.ARTICLE_IMAGE;
import static com.ice.generated.jooq.tables.ArticleTag.ARTICLE_TAG;
import static com.ice.generated.jooq.tables.Category.CATEGORY;
import static com.ice.generated.jooq.tables.ContentReview.CONTENT_REVIEW;
import static com.ice.generated.jooq.tables.Tag.TAG;
import static com.ice.generated.jooq.tables.User.USER;

@Service
public class ArticleService {

    private final DSLContext dsl;
    private final AiAuditClient aiAuditClient;
    private final NotificationService notificationService;
    private final BookCoinService bookCoinService;
    private final ObjectMapper objectMapper;

    private final RankingConfigService rankingConfigService;

    public ArticleService(
            DSLContext dsl,
            AiAuditClient aiAuditClient,
            NotificationService notificationService,
            BookCoinService bookCoinService,
            ObjectMapper objectMapper,
            RankingConfigService rankingConfigService
    ) {
        this.dsl = dsl;
        this.aiAuditClient = aiAuditClient;
        this.notificationService = notificationService;
        this.bookCoinService = bookCoinService;
        this.objectMapper = objectMapper;
        this.rankingConfigService = rankingConfigService;
    }

    @Transactional
    public CreateDraftResponse createDraft(long userId, CreateDraftRequest request) {
        validateDraftRequest(request);
        Long categoryId = resolveCategoryId(request.category_id());
        LocalDateTime now = LocalDateTime.now();
        int wordCount = request.content() == null ? 0 : request.content().length();

        Long articleId = dsl.insertInto(ARTICLE)
                .set(ARTICLE.USER_ID, userId)
                .set(ARTICLE.TITLE, request.title().trim())
                .set(ARTICLE.CONTENT, request.content())
                .set(ARTICLE.COVER_URL, request.cover_url())
                .set(ARTICLE.CATEGORY_ID, categoryId)
                .set(ARTICLE.TOPIC_ID, request.topic_id())
                .set(ARTICLE.WORD_COUNT, wordCount)
                .set(ARTICLE.STATUS, ArticleStatus.DRAFT.code())
                .set(ARTICLE.CREATED_AT, now)
                .set(ARTICLE.UPDATED_AT, now)
                .returningResult(ARTICLE.ID)
                .fetchOne(ARTICLE.ID);

        replaceTags(articleId, request.tag_ids());
        replaceImages(articleId, request.image_urls());

        return new CreateDraftResponse(
                articleId,
                ArticleStatus.DRAFT.apiValue(),
                wordCount,
                toInstant(now)
        );
    }

    @Transactional
    public CreateDraftResponse updateDraft(long userId, long articleId, CreateDraftRequest request) {
        ArticleRecord article = requireOwnedArticle(userId, articleId);
        ArticleStatus status = ArticleStatus.fromCode(article.getStatus());
        if (status != ArticleStatus.DRAFT && status != ArticleStatus.REJECTED) {
            throw new ApiException("ARTICLE_NOT_EDITABLE", "当前状态不可编辑", HttpStatus.CONFLICT);
        }
        validateDraftRequest(request);
        Long categoryId = resolveCategoryId(request.category_id());
        LocalDateTime now = LocalDateTime.now();
        int wordCount = request.content() == null ? 0 : request.content().length();
        dsl.update(ARTICLE)
                .set(ARTICLE.TITLE, request.title().trim())
                .set(ARTICLE.CONTENT, request.content())
                .set(ARTICLE.COVER_URL, request.cover_url())
                .set(ARTICLE.CATEGORY_ID, categoryId)
                .set(ARTICLE.TOPIC_ID, request.topic_id())
                .set(ARTICLE.WORD_COUNT, wordCount)
                .set(ARTICLE.UPDATED_AT, now)
                .execute();
        replaceTags(articleId, request.tag_ids());
        replaceImages(articleId, request.image_urls());
        return new CreateDraftResponse(
                articleId,
                status.apiValue(),
                wordCount,
                toInstant(now)
        );
    }

    @Transactional
    public ReviewAppealResponse reviewAppeal(long userId, long articleId, ReviewAppealRequest request) {
        ArticleRecord article = requireOwnedArticle(userId, articleId);
        if (ArticleStatus.fromCode(article.getStatus()) != ArticleStatus.REJECTED) {
            throw new ApiException("INVALID_APPEAL", "仅已拒绝文章可申请复审", HttpStatus.BAD_REQUEST);
        }
        if (request.appeal_text() == null || request.appeal_text().isBlank()) {
            throw new ApiException("VALIDATION_ERROR", "请填写申诉说明", HttpStatus.BAD_REQUEST);
        }
        boolean manual = "manual".equalsIgnoreCase(request.appeal_type());
        int cost = manual ? 30 : 10;
        bookCoinService.spend(
                userId,
                cost,
                manual ? "manual_review_appeal" : "ai_review_appeal",
                "article",
                articleId,
                manual ? "人工复审申诉" : "AI复审申诉"
        );

        LocalDateTime now = LocalDateTime.now();
        byte reviewType = manual ? ReviewType.MANUAL.code() : ReviewType.AI_APPEAL.code();
        Long reviewId = dsl.insertInto(CONTENT_REVIEW)
                .set(CONTENT_REVIEW.CONTENT_TYPE, ContentType.ARTICLE.code())
                .set(CONTENT_REVIEW.CONTENT_ID, articleId)
                .set(CONTENT_REVIEW.REVIEW_TYPE, reviewType)
                .set(CONTENT_REVIEW.STATUS, ReviewStatus.PENDING.code())
                .set(CONTENT_REVIEW.APPEAL_TEXT, request.appeal_text().trim())
                .set(CONTENT_REVIEW.CREATED_AT, now)
                .returningResult(CONTENT_REVIEW.ID)
                .fetchOne(CONTENT_REVIEW.ID);

        dsl.update(ARTICLE)
                .set(ARTICLE.STATUS, ArticleStatus.REVIEWING.code())
                .set(ARTICLE.UPDATED_AT, now)
                .where(ARTICLE.ID.eq(articleId))
                .execute();

        if (!manual) {
            List<String> tagNames = loadTagNames(articleId);
            AiAuditResponse aiResponse = aiAuditClient.audit(
                    String.valueOf(reviewId),
                    ReviewType.AI_APPEAL.apiValue(),
                    article.getTitle(),
                    article.getContent(),
                    tagNames
            );
            applyAuditResult(articleId, userId, article.getTitle(), reviewId, aiResponse);
        }

        return new ReviewAppealResponse(
                reviewId,
                ArticleStatus.REVIEWING.apiValue(),
                manual ? "已提交人工复审" : "已提交AI复审"
        );
    }

    @Transactional
    public PublishResponse publish(long userId, long articleId) {
        ArticleRecord article = requireOwnedArticle(userId, articleId);
        byte status = article.getStatus();
        if (status != ArticleStatus.DRAFT.code() && status != ArticleStatus.REJECTED.code()) {
            throw new ApiException("INVALID_STATUS", "当前状态不可发布", HttpStatus.BAD_REQUEST);
        }
        if (article.getTitle() == null || article.getTitle().isBlank()) {
            throw new ApiException("VALIDATION_ERROR", "标题不能为空", HttpStatus.BAD_REQUEST);
        }
        if (article.getContent() == null || article.getContent().isBlank()) {
            throw new ApiException("VALIDATION_ERROR", "正文不能为空", HttpStatus.BAD_REQUEST);
        }
        List<String> tagNames = loadTagNames(articleId);
        if (tagNames.isEmpty()) {
            throw new ApiException("VALIDATION_ERROR", "请至少选择 1 个标签", HttpStatus.BAD_REQUEST);
        }

        LocalDateTime now = LocalDateTime.now();
        dsl.update(ARTICLE)
                .set(ARTICLE.STATUS, ArticleStatus.REVIEWING.code())
                .set(ARTICLE.REJECT_REASON, (String) null)
                .set(ARTICLE.UPDATED_AT, now)
                .where(ARTICLE.ID.eq(articleId))
                .execute();

        Long reviewId = dsl.insertInto(CONTENT_REVIEW)
                .set(CONTENT_REVIEW.CONTENT_TYPE, ContentType.ARTICLE.code())
                .set(CONTENT_REVIEW.CONTENT_ID, articleId)
                .set(CONTENT_REVIEW.REVIEW_TYPE, ReviewType.INITIAL_AI.code())
                .set(CONTENT_REVIEW.STATUS, ReviewStatus.PENDING.code())
                .set(CONTENT_REVIEW.CREATED_AT, now)
                .returningResult(CONTENT_REVIEW.ID)
                .fetchOne(CONTENT_REVIEW.ID);

        AiAuditResponse aiResponse = aiAuditClient.audit(
                String.valueOf(reviewId),
                ReviewType.INITIAL_AI.apiValue(),
                article.getTitle(),
                article.getContent(),
                tagNames
        );

        return applyAuditResult(articleId, article.getUserId(), article.getTitle(), reviewId, aiResponse);
    }

    public FeaturedArticlesResponse listFeatured(int limit) {
        int featuredDays = rankingConfigService.getInt("featured_days", 7);
        double aiWeight = rankingConfigService.getDouble("featured_ai_weight", 0.7);
        double freshWeight = rankingConfigService.getDouble("featured_fresh_weight", 0.3);
        LocalDateTime since = LocalDateTime.now().minusDays(featuredDays);

        var records = dsl.select(
                        ARTICLE.ID,
                        ARTICLE.TITLE,
                        ARTICLE.COVER_URL,
                        ARTICLE.AI_QUALITY_SCORE,
                        ARTICLE.PUBLISHED_AT,
                        USER.NICKNAME,
                        CATEGORY.NAME
                )
                .from(ARTICLE)
                .join(USER).on(USER.ID.eq(ARTICLE.USER_ID))
                .leftJoin(CATEGORY).on(CATEGORY.ID.eq(ARTICLE.CATEGORY_ID))
                .where(ARTICLE.STATUS.eq(ArticleStatus.PUBLISHED.code()))
                .and(ARTICLE.PUBLISHED_AT.ge(since))
                .fetch();

        List<ScoredFeatured> scored = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (var record : records) {
            int aiScore = record.get(ARTICLE.AI_QUALITY_SCORE) == null ? 70 : record.get(ARTICLE.AI_QUALITY_SCORE).intValue();
            LocalDateTime publishedAt = record.get(ARTICLE.PUBLISHED_AT);
            long hours = publishedAt == null ? 0 : java.time.Duration.between(publishedAt, now).toHours();
            double freshness = Math.max(0, 100 - hours * 2.0);
            double featuredScore = aiScore * aiWeight + freshness * freshWeight;
            scored.add(new ScoredFeatured(
                    record.get(ARTICLE.ID),
                    record.get(ARTICLE.TITLE),
                    record.get(ARTICLE.COVER_URL),
                    record.get(USER.NICKNAME),
                    record.get(CATEGORY.NAME),
                    featuredScore,
                    publishedAt
            ));
        }
        scored.sort(Comparator.comparingDouble(ScoredFeatured::featuredScore).reversed());
        List<FeaturedArticleItemDto> items = new ArrayList<>();
        for (ScoredFeatured article : scored) {
            if (items.size() >= limit) {
                break;
            }
            items.add(new FeaturedArticleItemDto(
                    article.id(),
                    article.title(),
                    article.coverUrl(),
                    article.authorNickname(),
                    article.categoryName() == null ? "综合" : article.categoryName(),
                    Math.round(article.featuredScore() * 10.0) / 10.0,
                    article.publishedAt() == null ? null : toInstant(article.publishedAt()).toString()
            ));
        }
        return new FeaturedArticlesResponse(items);
    }

    public ArticleDetailResponse getArticle(long userId, long articleId) {
        ArticleRecord article = dsl.selectFrom(ARTICLE)
                .where(ARTICLE.ID.eq(articleId))
                .fetchOne();
        if (article == null) {
            throw new ApiException("ARTICLE_NOT_FOUND", "文章不存在", HttpStatus.NOT_FOUND);
        }
        ArticleStatus status = ArticleStatus.fromCode(article.getStatus());
        if (status != ArticleStatus.PUBLISHED && !article.getUserId().equals(userId)) {
            throw new ApiException("FORBIDDEN", "无权限查看该文章", HttpStatus.FORBIDDEN);
        }
        ContentReviewRecord latestReview = dsl.selectFrom(CONTENT_REVIEW)
                .where(CONTENT_REVIEW.CONTENT_ID.eq(articleId))
                .and(CONTENT_REVIEW.CONTENT_TYPE.eq(ContentType.ARTICLE.code()))
                .orderBy(CONTENT_REVIEW.ID.desc())
                .limit(1)
                .fetchOne();

        LatestReviewDto latestReviewDto = null;
        if (latestReview != null && latestReview.getCompletedAt() != null) {
            ReviewType reviewType = ReviewType.INITIAL_AI;
            for (ReviewType type : ReviewType.values()) {
                if (type.code() == latestReview.getReviewType()) {
                    reviewType = type;
                    break;
                }
            }
            latestReviewDto = new LatestReviewDto(
                    reviewType.apiValue(),
                    latestReview.getAiScore() == null ? null : latestReview.getAiScore().intValue(),
                    toInstant(latestReview.getCompletedAt())
            );
        }

        return new ArticleDetailResponse(
                article.getId(),
                article.getTitle(),
                article.getContent(),
                status.apiValue(),
                article.getRejectReason(),
                article.getAiQualityScore() == null ? null : article.getAiQualityScore().intValue(),
                loadTagDtos(articleId),
                latestReviewDto,
                article.getPublishedAt() == null ? null : toInstant(article.getPublishedAt()),
                toInstant(article.getCreatedAt())
        );
    }

    private PublishResponse applyAuditResult(
            long articleId,
            long userId,
            String title,
            long reviewId,
            AiAuditResponse aiResponse
    ) {
        if (aiResponse == null) {
            return new PublishResponse(
                    articleId,
                    ArticleStatus.REVIEWING.apiValue(),
                    null,
                    null,
                    "已提交审核，请稍候"
            );
        }

        LocalDateTime completedAt = LocalDateTime.now();
        String dimensionsJson = toJson(aiResponse.dimensions());
        JSON dimensions = dimensionsJson == null ? null : JSON.json(dimensionsJson);

        if ("pass".equals(aiResponse.result())) {
            dsl.update(CONTENT_REVIEW)
                    .set(CONTENT_REVIEW.STATUS, ReviewStatus.PASSED.code())
                    .set(CONTENT_REVIEW.AI_SCORE, (short) aiResponse.overall_score())
                    .set(CONTENT_REVIEW.AI_DIMENSIONS, dimensions)
                    .set(CONTENT_REVIEW.COMPLETED_AT, completedAt)
                    .where(CONTENT_REVIEW.ID.eq(reviewId))
                    .execute();

            dsl.update(ARTICLE)
                    .set(ARTICLE.STATUS, ArticleStatus.PUBLISHED.code())
                    .set(ARTICLE.AI_QUALITY_SCORE, (short) aiResponse.overall_score())
                    .set(ARTICLE.PUBLISHED_AT, completedAt)
                    .set(ARTICLE.UPDATED_AT, completedAt)
                    .where(ARTICLE.ID.eq(articleId))
                    .execute();

            dsl.update(com.ice.generated.jooq.tables.User.USER)
                    .set(com.ice.generated.jooq.tables.User.USER.ARTICLE_COUNT,
                            com.ice.generated.jooq.tables.User.USER.ARTICLE_COUNT.plus(1))
                    .where(com.ice.generated.jooq.tables.User.USER.ID.eq(userId))
                    .execute();

            notificationService.sendAuditPass(userId, articleId, title);
            bookCoinService.rewardPublishIfEligible(userId, articleId);

            return new PublishResponse(
                    articleId,
                    ArticleStatus.PUBLISHED.apiValue(),
                    toInstant(completedAt),
                    null,
                    null
            );
        }

        if ("manual".equals(aiResponse.result())) {
            dsl.update(CONTENT_REVIEW)
                    .set(CONTENT_REVIEW.AI_SCORE, (short) aiResponse.overall_score())
                    .set(CONTENT_REVIEW.AI_DIMENSIONS, dimensions)
                    .where(CONTENT_REVIEW.ID.eq(reviewId))
                    .execute();

            dsl.update(ARTICLE)
                    .set(ARTICLE.STATUS, ArticleStatus.REVIEWING.code())
                    .set(ARTICLE.UPDATED_AT, completedAt)
                    .where(ARTICLE.ID.eq(articleId))
                    .execute();

            return new PublishResponse(
                    articleId,
                    ArticleStatus.REVIEWING.apiValue(),
                    null,
                    null,
                    "已提交审核，请稍候"
            );
        }

        String rejectReason = aiResponse.reject_reason() != null
                ? aiResponse.reject_reason()
                : "未通过社区内容审核";

        dsl.update(CONTENT_REVIEW)
                .set(CONTENT_REVIEW.STATUS, ReviewStatus.REJECTED.code())
                .set(CONTENT_REVIEW.AI_SCORE, (short) aiResponse.overall_score())
                .set(CONTENT_REVIEW.AI_DIMENSIONS, dimensions)
                .set(CONTENT_REVIEW.REJECT_REASON, rejectReason)
                .set(CONTENT_REVIEW.COMPLETED_AT, completedAt)
                .where(CONTENT_REVIEW.ID.eq(reviewId))
                .execute();

        dsl.update(ARTICLE)
                .set(ARTICLE.STATUS, ArticleStatus.REJECTED.code())
                .set(ARTICLE.REJECT_REASON, rejectReason)
                .set(ARTICLE.UPDATED_AT, completedAt)
                .where(ARTICLE.ID.eq(articleId))
                .execute();

        notificationService.sendAuditReject(userId, articleId, title, rejectReason);

        return new PublishResponse(
                articleId,
                ArticleStatus.REJECTED.apiValue(),
                null,
                rejectReason,
                null
        );
    }

    private ArticleRecord requireOwnedArticle(long userId, long articleId) {
        ArticleRecord article = dsl.selectFrom(ARTICLE)
                .where(ARTICLE.ID.eq(articleId))
                .fetchOne();
        if (article == null) {
            throw new ApiException("ARTICLE_NOT_FOUND", "文章不存在", HttpStatus.NOT_FOUND);
        }
        if (!article.getUserId().equals(userId)) {
            throw new ApiException("FORBIDDEN", "无权限操作该文章", HttpStatus.FORBIDDEN);
        }
        return article;
    }

    private void validateDraftRequest(CreateDraftRequest request) {
        if (request.title() == null || request.title().isBlank()) {
            throw new ApiException("VALIDATION_ERROR", "标题不能为空", HttpStatus.BAD_REQUEST);
        }
        if (request.title().trim().length() > 15) {
            throw new ApiException("VALIDATION_ERROR", "标题最多 15 字", HttpStatus.BAD_REQUEST);
        }
        if (request.content() == null || request.content().isBlank()) {
            throw new ApiException("VALIDATION_ERROR", "正文不能为空", HttpStatus.BAD_REQUEST);
        }
        if (request.tag_ids() != null && request.tag_ids().size() > 5) {
            throw new ApiException("VALIDATION_ERROR", "标签最多 5 个", HttpStatus.BAD_REQUEST);
        }
    }

    private Long resolveCategoryId(Long categoryId) {
        if (categoryId != null) {
            return categoryId;
        }
        return dsl.select(CATEGORY.ID)
                .from(CATEGORY)
                .where(CATEGORY.IS_DEFAULT.eq((byte) 1))
                .fetchOne(CATEGORY.ID);
    }

    private void replaceTags(long articleId, List<Long> tagIds) {
        dsl.deleteFrom(ARTICLE_TAG).where(ARTICLE_TAG.ARTICLE_ID.eq(articleId)).execute();
        if (tagIds == null) {
            return;
        }
        for (Long tagId : tagIds) {
            dsl.insertInto(ARTICLE_TAG)
                    .set(ARTICLE_TAG.ARTICLE_ID, articleId)
                    .set(ARTICLE_TAG.TAG_ID, tagId)
                    .execute();
        }
    }

    private void replaceImages(long articleId, List<String> imageUrls) {
        dsl.deleteFrom(ARTICLE_IMAGE).where(ARTICLE_IMAGE.ARTICLE_ID.eq(articleId)).execute();
        if (imageUrls == null || imageUrls.isEmpty()) {
            return;
        }
        byte order = 1;
        for (String url : imageUrls) {
            if (url == null || url.isBlank()) {
                continue;
            }
            dsl.insertInto(ARTICLE_IMAGE)
                    .set(ARTICLE_IMAGE.ARTICLE_ID, articleId)
                    .set(ARTICLE_IMAGE.URL, url)
                    .set(ARTICLE_IMAGE.SORT_ORDER, order++)
                    .execute();
        }
    }

    private List<String> loadTagNames(long articleId) {
        return dsl.select(TAG.NAME)
                .from(ARTICLE_TAG)
                .join(TAG).on(TAG.ID.eq(ARTICLE_TAG.TAG_ID))
                .where(ARTICLE_TAG.ARTICLE_ID.eq(articleId))
                .fetch(TAG.NAME);
    }

    private List<TagDto> loadTagDtos(long articleId) {
        return dsl.select(TAG.ID, TAG.NAME)
                .from(ARTICLE_TAG)
                .join(TAG).on(TAG.ID.eq(ARTICLE_TAG.TAG_ID))
                .where(ARTICLE_TAG.ARTICLE_ID.eq(articleId))
                .fetch(record -> new TagDto(record.get(TAG.ID), record.get(TAG.NAME)));
    }

    private String toJson(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException ex) {
            return null;
        }
    }

    private Instant toInstant(LocalDateTime time) {
        return time.atOffset(ZoneOffset.UTC).toInstant();
    }

    private record ScoredFeatured(
            long id,
            String title,
            String coverUrl,
            String authorNickname,
            String categoryName,
            double featuredScore,
            LocalDateTime publishedAt
    ) {
    }
}
