package com.ice.service.admin;

import com.ice.domain.ArticleStatus;
import com.ice.domain.ContentType;
import com.ice.domain.ReviewStatus;
import com.ice.domain.ReviewType;
import com.ice.dto.admin.ReviewDetailDto;
import com.ice.dto.admin.ReviewListItemDto;
import com.ice.dto.admin.ReviewListResponse;
import com.ice.dto.admin.ReviewRejectRequest;
import com.ice.exception.ApiException;
import com.ice.service.NotificationService;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import org.jooq.DSLContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ice.generated.jooq.tables.Article.ARTICLE;
import static com.ice.generated.jooq.tables.ContentReview.CONTENT_REVIEW;

@Service
public class AdminReviewService {

    private static final List<Byte> PENDING_STATUS = List.of(ReviewStatus.PENDING.code());

    private final DSLContext dsl;
    private final NotificationService notificationService;

    public AdminReviewService(DSLContext dsl, NotificationService notificationService) {
        this.dsl = dsl;
        this.notificationService = notificationService;
    }

    public ReviewListResponse list(int page, int size) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.min(Math.max(size, 1), 50);
        int offset = (safePage - 1) * safeSize;

        long total = dsl.fetchCount(
                dsl.selectFrom(CONTENT_REVIEW).where(CONTENT_REVIEW.STATUS.in(PENDING_STATUS))
        );

        List<ReviewListItemDto> items = dsl.selectFrom(CONTENT_REVIEW)
                .where(CONTENT_REVIEW.STATUS.in(PENDING_STATUS))
                .orderBy(CONTENT_REVIEW.REVIEW_TYPE.desc(), CONTENT_REVIEW.CREATED_AT.asc())
                .limit(safeSize)
                .offset(offset)
                .fetch(this::toListItem);

        items = items.stream()
                .sorted(Comparator.comparingInt(ReviewListItemDto::priority).reversed()
                        .thenComparing(ReviewListItemDto::created_at))
                .toList();

        return new ReviewListResponse(items, total, safePage, safeSize);
    }

    public ReviewDetailDto get(long id) {
        var review = dsl.selectFrom(CONTENT_REVIEW).where(CONTENT_REVIEW.ID.eq(id)).fetchOne();
        if (review == null) {
            throw new ApiException("NOT_FOUND", "审核记录不存在", HttpStatus.NOT_FOUND);
        }
        var article = dsl.selectFrom(ARTICLE).where(ARTICLE.ID.eq(review.getContentId())).fetchOne();
        return new ReviewDetailDto(
                review.getId(),
                review.getContentId(),
                article == null ? null : article.getTitle(),
                article == null ? null : article.getContent(),
                review.getReviewType() == null ? null : (int) review.getReviewType(),
                review.getAppealText(),
                review.getAiScore() == null ? null : review.getAiScore().intValue(),
                review.getAiDimensions() == null ? null : review.getAiDimensions().data(),
                toInstant(review.getCreatedAt())
        );
    }

    @Transactional
    public void approve(long reviewId, long reviewerId) {
        var review = requirePendingReview(reviewId);
        LocalDateTime now = LocalDateTime.now();
        dsl.update(CONTENT_REVIEW)
                .set(CONTENT_REVIEW.STATUS, ReviewStatus.PASSED.code())
                .set(CONTENT_REVIEW.REVIEWER_ID, reviewerId)
                .set(CONTENT_REVIEW.COMPLETED_AT, now)
                .where(CONTENT_REVIEW.ID.eq(reviewId))
                .execute();
        if (review.getContentType() == ContentType.ARTICLE.code()) {
            var article = dsl.selectFrom(ARTICLE).where(ARTICLE.ID.eq(review.getContentId())).fetchOne();
            dsl.update(ARTICLE)
                    .set(ARTICLE.STATUS, ArticleStatus.PUBLISHED.code())
                    .set(ARTICLE.PUBLISHED_AT, now)
                    .set(ARTICLE.REJECT_REASON, (String) null)
                    .set(ARTICLE.UPDATED_AT, now)
                    .where(ARTICLE.ID.eq(review.getContentId()))
                    .execute();
            if (article != null) {
                notificationService.sendAuditPass(article.getUserId(), article.getId(), article.getTitle());
            }
        }
    }

    @Transactional
    public void reject(long reviewId, long reviewerId, ReviewRejectRequest request) {
        var review = requirePendingReview(reviewId);
        String reason = resolveRejectReason(request);
        LocalDateTime now = LocalDateTime.now();
        dsl.update(CONTENT_REVIEW)
                .set(CONTENT_REVIEW.STATUS, ReviewStatus.REJECTED.code())
                .set(CONTENT_REVIEW.REVIEWER_ID, reviewerId)
                .set(CONTENT_REVIEW.REJECT_REASON, reason)
                .set(CONTENT_REVIEW.COMPLETED_AT, now)
                .where(CONTENT_REVIEW.ID.eq(reviewId))
                .execute();
        if (review.getContentType() == ContentType.ARTICLE.code()) {
            var article = dsl.selectFrom(ARTICLE).where(ARTICLE.ID.eq(review.getContentId())).fetchOne();
            dsl.update(ARTICLE)
                    .set(ARTICLE.STATUS, ArticleStatus.REJECTED.code())
                    .set(ARTICLE.REJECT_REASON, reason)
                    .set(ARTICLE.UPDATED_AT, now)
                    .where(ARTICLE.ID.eq(review.getContentId()))
                    .execute();
            if (article != null) {
                notificationService.sendAuditReject(article.getUserId(), article.getId(), article.getTitle(), reason);
            }
        }
    }

    private com.ice.generated.jooq.tables.records.ContentReviewRecord requirePendingReview(long reviewId) {
        var review = dsl.selectFrom(CONTENT_REVIEW).where(CONTENT_REVIEW.ID.eq(reviewId)).fetchOne();
        if (review == null) {
            throw new ApiException("NOT_FOUND", "审核记录不存在", HttpStatus.NOT_FOUND);
        }
        if (!PENDING_STATUS.contains(review.getStatus())) {
            throw new ApiException("INVALID_STATUS", "该记录已处理", HttpStatus.CONFLICT);
        }
        return review;
    }

    private ReviewListItemDto toListItem(com.ice.generated.jooq.tables.records.ContentReviewRecord record) {
        int priority = record.getReviewType() == ReviewType.MANUAL.code() ? 2 : 1;
        String title = dsl.select(ARTICLE.TITLE)
                .from(ARTICLE)
                .where(ARTICLE.ID.eq(record.getContentId()))
                .fetchOne(ARTICLE.TITLE);
        return new ReviewListItemDto(
                record.getId(),
                record.getContentId(),
                title,
                record.getReviewType() == null ? 0 : (int) record.getReviewType(),
                priority,
                toInstant(record.getCreatedAt())
        );
    }

    private String resolveRejectReason(ReviewRejectRequest request) {
        if (request == null || request.reason_code() == null || request.reason_code().isBlank()) {
            throw new ApiException("VALIDATION_ERROR", "请选择拒绝原因", HttpStatus.BAD_REQUEST);
        }
        return switch (request.reason_code()) {
            case "illegal" -> "含有违法违规内容";
            case "porn" -> "含有色情低俗内容";
            case "spam" -> "垃圾广告";
            case "plagiarism" -> "涉嫌抄袭";
            case "abuse" -> "人身攻击";
            case "misinfo" -> "虚假信息";
            case "other" -> request.reason_text() == null || request.reason_text().isBlank()
                    ? "其他原因" : request.reason_text().trim();
            default -> throw new ApiException("VALIDATION_ERROR", "无效拒绝原因", HttpStatus.BAD_REQUEST);
        };
    }

    private Instant toInstant(LocalDateTime time) {
        return time == null ? null : time.atOffset(ZoneOffset.UTC).toInstant();
    }
}
