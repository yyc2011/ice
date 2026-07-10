package com.ice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ice.client.AiAuditClient;
import com.ice.client.dto.AiAuditResponse;
import com.ice.domain.ArticleStatus;
import com.ice.domain.ContentType;
import com.ice.domain.ReviewStatus;
import com.ice.domain.ReviewType;
import com.ice.domain.TopicStatus;
import com.ice.domain.UserLevel;
import com.ice.dto.topic.CreateTopicRequest;
import com.ice.dto.topic.CreateTopicResponse;
import com.ice.dto.topic.TopicDetailResponse;
import com.ice.dto.topic.TopicItemDto;
import com.ice.dto.topic.TopicListResponse;
import com.ice.exception.ApiException;
import com.ice.generated.jooq.tables.records.TopicRecord;
import com.ice.generated.jooq.tables.records.UserRecord;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.jooq.DSLContext;
import org.jooq.JSON;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ice.generated.jooq.tables.Article.ARTICLE;
import static com.ice.generated.jooq.tables.ContentReview.CONTENT_REVIEW;
import static com.ice.generated.jooq.tables.Topic.TOPIC;
import static com.ice.generated.jooq.tables.User.USER;

@Service
public class TopicService {

    private final DSLContext dsl;
    private final AiAuditClient aiAuditClient;
    private final BookCoinService bookCoinService;
    private final ObjectMapper objectMapper;

    public TopicService(
            DSLContext dsl,
            AiAuditClient aiAuditClient,
            BookCoinService bookCoinService,
            ObjectMapper objectMapper
    ) {
        this.dsl = dsl;
        this.aiAuditClient = aiAuditClient;
        this.bookCoinService = bookCoinService;
        this.objectMapper = objectMapper;
    }

    public TopicListResponse listOngoing(int limit) {
        return listByStatus("ongoing", limit, true);
    }

    public TopicListResponse list(String status, int limit) {
        return listByStatus(status, limit, false);
    }

    public TopicListResponse listRandomEnded(int limit) {
        List<TopicRecord> all = dsl.selectFrom(TOPIC)
                .where(TOPIC.STATUS.eq(TopicStatus.ENDED.code()))
                .fetch();
        if (all.isEmpty()) {
            return new TopicListResponse(List.of(), 0);
        }
        List<TopicRecord> shuffled = new ArrayList<>(all);
        java.util.Collections.shuffle(shuffled, ThreadLocalRandom.current());
        List<TopicItemDto> items = new ArrayList<>();
        for (int i = 0; i < Math.min(limit, shuffled.size()); i++) {
            items.add(toItemDto(shuffled.get(i)));
        }
        return new TopicListResponse(items, all.size());
    }

    public TopicListResponse listHistorical(String period, int limit) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime since = switch (period == null ? "today" : period) {
            case "month" -> now.minusDays(30);
            case "year" -> now.minusDays(365);
            default -> now.minusDays(1);
        };
        List<TopicRecord> records = dsl.selectFrom(TOPIC)
                .where(TOPIC.STATUS.eq(TopicStatus.ENDED.code()))
                .and(TOPIC.END_AT.ge(since))
                .orderBy(TOPIC.ARTICLE_COUNT.desc(), TOPIC.END_AT.desc())
                .limit(limit)
                .fetch();
        List<TopicItemDto> items = records.stream().map(this::toItemDto).toList();
        return new TopicListResponse(items, items.size());
    }

    public TopicDetailResponse getDetail(long topicId) {
        TopicRecord topic = requireTopic(topicId);
        UserRecord creator = dsl.selectFrom(USER).where(USER.ID.eq(topic.getUserId())).fetchOne();
        return toDetailDto(topic, creator == null ? "未知" : creator.getNickname());
    }

    public TopicListResponse listTopicArticles(long topicId, int page, int size) {
        requireTopic(topicId);
        int offset = (Math.max(page, 1) - 1) * size;
        var records = dsl.selectFrom(ARTICLE)
                .where(ARTICLE.TOPIC_ID.eq(topicId))
                .and(ARTICLE.STATUS.eq(ArticleStatus.PUBLISHED.code()))
                .orderBy(ARTICLE.PUBLISHED_AT.desc())
                .limit(size)
                .offset(offset)
                .fetch();
        // Reuse TopicItemDto shape is wrong - return empty for now, articles use article list elsewhere
        return new TopicListResponse(List.of(), records.size());
    }

    public TopicListResponse listSelectable() {
        LocalDateTime now = LocalDateTime.now();
        List<TopicRecord> records = dsl.selectFrom(TOPIC)
                .where(TOPIC.STATUS.eq(TopicStatus.ONGOING.code()))
                .and(TOPIC.START_AT.le(now))
                .and(TOPIC.END_AT.gt(now))
                .orderBy(TOPIC.ARTICLE_COUNT.desc())
                .fetch();
        return new TopicListResponse(records.stream().map(this::toItemDto).toList(), records.size());
    }

    public TopicListResponse listMyTopics(long userId, String role) {
        var query = dsl.selectFrom(TOPIC).where(TOPIC.USER_ID.eq(userId));
        if ("joined".equals(role)) {
            List<Long> topicIds = dsl.selectDistinct(ARTICLE.TOPIC_ID)
                    .from(ARTICLE)
                    .where(ARTICLE.USER_ID.eq(userId))
                    .and(ARTICLE.TOPIC_ID.isNotNull())
                    .fetch(ARTICLE.TOPIC_ID);
            if (topicIds.isEmpty()) {
                return new TopicListResponse(List.of(), 0);
            }
            query = dsl.selectFrom(TOPIC).where(TOPIC.ID.in(topicIds));
        }
        List<TopicRecord> records = query.orderBy(TOPIC.UPDATED_AT.desc()).fetch();
        return new TopicListResponse(records.stream().map(this::toItemDto).toList(), records.size());
    }

    @Transactional
    public CreateTopicResponse createTopic(long userId, CreateTopicRequest request) {
        UserRecord user = dsl.selectFrom(USER).where(USER.ID.eq(userId)).fetchOne();
        if (user == null) {
            throw new ApiException("UNAUTHORIZED", "用户不存在", HttpStatus.UNAUTHORIZED);
        }
        byte levelCode = user.getLevel() == null ? UserLevel.READER.code() : user.getLevel().byteValue();
        if (!UserLevel.canCreateTopic(levelCode)) {
            throw new ApiException("FORBIDDEN", "达到成长等级后可发起话题", HttpStatus.FORBIDDEN);
        }
        if (request.title() == null || request.title().isBlank()) {
            throw new ApiException("VALIDATION_ERROR", "话题标题不能为空", HttpStatus.BAD_REQUEST);
        }
        int durationDays = request.duration_days() == null ? 14 : request.duration_days();
        int rewardPool = request.reward_pool_amount() == null ? 0 : request.reward_pool_amount();
        int totalCost = 20 + rewardPool;
        bookCoinService.spend(userId, totalCost, "cost_start_topic", "topic", null, "发起话题");

        LocalDateTime now = LocalDateTime.now();
        Long topicId = dsl.insertInto(TOPIC)
                .set(TOPIC.USER_ID, userId)
                .set(TOPIC.TITLE, request.title().trim())
                .set(TOPIC.DESCRIPTION, request.description())
                .set(TOPIC.COVER_URL, request.cover_url())
                .set(TOPIC.DURATION_DAYS, durationDays)
                .set(TOPIC.REWARD_POOL_AMOUNT, rewardPool)
                .set(TOPIC.REWARD_TOP_N, request.reward_top_n() == null ? (byte) 3 : request.reward_top_n().byteValue())
                .set(TOPIC.REWARD_RATIO, request.reward_ratio() == null ? "5:3:2" : request.reward_ratio())
                .set(TOPIC.STATUS, TopicStatus.REVIEWING.code())
                .set(TOPIC.CREATED_AT, now)
                .set(TOPIC.UPDATED_AT, now)
                .returningResult(TOPIC.ID)
                .fetchOne(TOPIC.ID);

        Long reviewId = dsl.insertInto(CONTENT_REVIEW)
                .set(CONTENT_REVIEW.CONTENT_TYPE, ContentType.TOPIC.code())
                .set(CONTENT_REVIEW.CONTENT_ID, topicId)
                .set(CONTENT_REVIEW.REVIEW_TYPE, ReviewType.INITIAL_AI.code())
                .set(CONTENT_REVIEW.STATUS, ReviewStatus.PENDING.code())
                .set(CONTENT_REVIEW.CREATED_AT, now)
                .returningResult(CONTENT_REVIEW.ID)
                .fetchOne(CONTENT_REVIEW.ID);

        AiAuditResponse aiResponse = aiAuditClient.auditTopic(
                String.valueOf(reviewId),
                ReviewType.INITIAL_AI.apiValue(),
                request.title().trim(),
                request.description() == null ? "" : request.description()
        );

        return applyTopicAuditResult(topicId, userId, request.title().trim(), totalCost, reviewId, aiResponse);
    }

    private TopicListResponse listByStatus(String status, int limit, boolean homeOrder) {
        LocalDateTime now = LocalDateTime.now();
        List<TopicRecord> records;
        if ("ongoing".equals(status)) {
            records = dsl.selectFrom(TOPIC)
                    .where(TOPIC.STATUS.eq(TopicStatus.ONGOING.code()))
                    .and(TOPIC.START_AT.le(now))
                    .and(TOPIC.END_AT.gt(now))
                    .fetch();
            records.sort(homeOrder
                    ? Comparator.comparingInt(TopicRecord::getArticleCount).reversed()
                        .thenComparing(TopicRecord::getStartAt, Comparator.nullsLast(Comparator.reverseOrder()))
                    : Comparator.comparingInt(TopicRecord::getArticleCount).reversed()
                        .thenComparing(TopicRecord::getEndAt, Comparator.nullsLast(Comparator.naturalOrder())));
        } else if ("upcoming".equals(status)) {
            records = dsl.selectFrom(TOPIC)
                    .where(TOPIC.STATUS.eq(TopicStatus.REVIEWING.code())
                            .or(TOPIC.STATUS.eq(TopicStatus.ONGOING.code()).and(TOPIC.START_AT.gt(now))))
                    .orderBy(TOPIC.START_AT.asc())
                    .limit(limit)
                    .fetch();
            List<TopicItemDto> items = records.stream().limit(limit).map(this::toItemDto).toList();
            return new TopicListResponse(items, items.size());
        } else if ("ended".equals(status)) {
            records = dsl.selectFrom(TOPIC)
                    .where(TOPIC.STATUS.eq(TopicStatus.ENDED.code()))
                    .orderBy(TOPIC.ARTICLE_COUNT.desc())
                    .limit(limit)
                    .fetch();
            List<TopicItemDto> items = records.stream().map(this::toItemDto).toList();
            return new TopicListResponse(items, items.size());
        } else {
            records = dsl.selectFrom(TOPIC)
                    .orderBy(TOPIC.CREATED_AT.desc())
                    .limit(limit)
                    .fetch();
            List<TopicItemDto> items = records.stream().map(this::toItemDto).toList();
            return new TopicListResponse(items, items.size());
        }
        List<TopicItemDto> items = records.stream().limit(limit).map(this::toItemDto).toList();
        return new TopicListResponse(items, items.size());
    }

    private CreateTopicResponse applyTopicAuditResult(
            long topicId,
            long userId,
            String title,
            int totalCost,
            long reviewId,
            AiAuditResponse aiResponse
    ) {
        if (aiResponse == null) {
            return new CreateTopicResponse(topicId, TopicStatus.REVIEWING.apiValue(), "已提交审核，请稍候");
        }
        LocalDateTime completedAt = LocalDateTime.now();
        String dimensionsJson = toJson(aiResponse.dimensions());
        JSON dimensions = dimensionsJson == null ? null : JSON.json(dimensionsJson);

        if ("pass".equals(aiResponse.result())) {
            TopicRecord topic = dsl.selectFrom(TOPIC).where(TOPIC.ID.eq(topicId)).fetchOne();
            int durationDays = topic == null ? 14 : topic.getDurationDays();
            LocalDateTime startAt = completedAt;
            LocalDateTime endAt = completedAt.plusDays(durationDays);

            dsl.update(CONTENT_REVIEW)
                    .set(CONTENT_REVIEW.STATUS, ReviewStatus.PASSED.code())
                    .set(CONTENT_REVIEW.AI_SCORE, (short) aiResponse.overall_score())
                    .set(CONTENT_REVIEW.AI_DIMENSIONS, dimensions)
                    .set(CONTENT_REVIEW.COMPLETED_AT, completedAt)
                    .where(CONTENT_REVIEW.ID.eq(reviewId))
                    .execute();

            dsl.update(TOPIC)
                    .set(TOPIC.STATUS, TopicStatus.ONGOING.code())
                    .set(TOPIC.START_AT, startAt)
                    .set(TOPIC.END_AT, endAt)
                    .set(TOPIC.UPDATED_AT, completedAt)
                    .where(TOPIC.ID.eq(topicId))
                    .execute();

            return new CreateTopicResponse(topicId, TopicStatus.ONGOING.apiValue(), "话题已发布");
        }

        if ("manual".equals(aiResponse.result())) {
            dsl.update(CONTENT_REVIEW)
                    .set(CONTENT_REVIEW.AI_SCORE, (short) aiResponse.overall_score())
                    .set(CONTENT_REVIEW.AI_DIMENSIONS, dimensions)
                    .where(CONTENT_REVIEW.ID.eq(reviewId))
                    .execute();
            return new CreateTopicResponse(topicId, TopicStatus.REVIEWING.apiValue(), "已提交审核，请稍候");
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

        dsl.update(TOPIC)
                .set(TOPIC.STATUS, TopicStatus.REJECTED.code())
                .set(TOPIC.REJECT_REASON, rejectReason)
                .set(TOPIC.UPDATED_AT, completedAt)
                .where(TOPIC.ID.eq(topicId))
                .execute();

        // Refund topic creation cost
        bookCoinService.refund(userId, totalCost, "topic_reject_refund", "topic", topicId, "话题审核拒绝退款");

        return new CreateTopicResponse(topicId, TopicStatus.REJECTED.apiValue(), rejectReason);
    }

    private TopicRecord requireTopic(long topicId) {
        TopicRecord topic = dsl.selectFrom(TOPIC).where(TOPIC.ID.eq(topicId)).fetchOne();
        if (topic == null) {
            throw new ApiException("TOPIC_NOT_FOUND", "话题不存在", HttpStatus.NOT_FOUND);
        }
        return topic;
    }

    private TopicItemDto toItemDto(TopicRecord topic) {
        TopicStatus status = TopicStatus.fromCode(topic.getStatus());
        return new TopicItemDto(
                topic.getId(),
                topic.getTitle(),
                topic.getDescription(),
                topic.getCoverUrl(),
                status.apiValue(),
                topic.getArticleCount() == null ? 0 : topic.getArticleCount(),
                topic.getViewCount() == null ? 0 : topic.getViewCount(),
                formatInstant(topic.getStartAt()),
                formatInstant(topic.getEndAt()),
                daysRemaining(topic.getEndAt())
        );
    }

    private TopicDetailResponse toDetailDto(TopicRecord topic, String creatorNickname) {
        TopicStatus status = TopicStatus.fromCode(topic.getStatus());
        return new TopicDetailResponse(
                topic.getId(),
                topic.getTitle(),
                topic.getDescription(),
                topic.getCoverUrl(),
                status.apiValue(),
                topic.getArticleCount() == null ? 0 : topic.getArticleCount(),
                topic.getViewCount() == null ? 0 : topic.getViewCount(),
                topic.getDurationDays() == null ? 14 : topic.getDurationDays(),
                topic.getRewardPoolAmount() == null ? 0 : topic.getRewardPoolAmount(),
                formatInstant(topic.getStartAt()),
                formatInstant(topic.getEndAt()),
                creatorNickname,
                daysRemaining(topic.getEndAt())
        );
    }

    private Integer daysRemaining(LocalDateTime endAt) {
        if (endAt == null) {
            return null;
        }
        long days = ChronoUnit.DAYS.between(LocalDateTime.now(), endAt);
        return (int) Math.max(days, 0);
    }

    private String formatInstant(LocalDateTime time) {
        if (time == null) {
            return null;
        }
        return time.atOffset(ZoneOffset.UTC).toInstant().toString();
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
}
