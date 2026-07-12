package com.ice.service;

import com.ice.domain.ArticleStatus;
import com.ice.dto.ranking.HotRankingItemDto;
import com.ice.dto.ranking.HotRankingResponse;
import com.ice.util.ContentSummary;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import static com.ice.generated.jooq.tables.Article.ARTICLE;
import static com.ice.generated.jooq.tables.User.USER;

@Service
public class RankingService {

    private final DSLContext dsl;
    private final RankingConfigService rankingConfigService;

    public RankingService(DSLContext dsl, RankingConfigService rankingConfigService) {
        this.dsl = dsl;
        this.rankingConfigService = rankingConfigService;
    }

    /** 展示用：不足 size 时按周期切片向前凑满，最近一周期固定靠前。 */
    public HotRankingResponse getHotRanking(String window, int limit) {
        return getHotRanking(window, limit, true);
    }

    /**
     * @param expandToFill true=展示凑满；false=结算，仅最近一个基础周期
     */
    public HotRankingResponse getHotRanking(String window, int limit, boolean expandToFill) {
        String resolvedWindow = window == null || window.isBlank() ? "24h" : window.trim();
        Duration base = resolveBaseDuration(resolvedWindow);
        LocalDateTime now = LocalDateTime.now();
        int minLikes = rankingConfigService.getInt("hot_min_likes", 1);
        int minViews = rankingConfigService.getInt("hot_min_views", 20);

        List<HotRankingItemDto> items = new ArrayList<>();
        Set<Long> seenIds = new HashSet<>();
        int periods = 1;

        while (items.size() < limit) {
            LocalDateTime end = periods == 1 ? now : now.minus(base.multipliedBy(periods - 1L));
            LocalDateTime start = now.minus(base.multipliedBy(periods));

            List<ScoredArticle> slice = scoreEligibleInRange(start, end, periods == 1, minLikes, minViews);
            slice.sort(Comparator.comparingDouble(ScoredArticle::hotScore).reversed());

            for (ScoredArticle article : slice) {
                if (items.size() >= limit) {
                    break;
                }
                if (!seenIds.add(article.id())) {
                    continue;
                }
                items.add(toItem(article, items.size() + 1));
            }

            if (!expandToFill) {
                break;
            }
            if (items.size() >= limit) {
                break;
            }
            if (!hasEligibleBefore(start, minLikes, minViews)) {
                break;
            }
            periods++;
        }

        return new HotRankingResponse(resolvedWindow, items);
    }

    public double calculateHotScore(int viewCount, int likeCount, int commentCount, int dislikeCount) {
        double wView = rankingConfigService.getDouble("hot_weight_view_ln", 10);
        double wLike = rankingConfigService.getDouble("hot_weight_like", 3);
        double wComment = rankingConfigService.getDouble("hot_weight_comment", 10);
        double wDislike = rankingConfigService.getDouble("hot_weight_dislike", 2);
        return Math.log(1 + viewCount) * wView
                + likeCount * wLike
                + commentCount * wComment
                - dislikeCount * wDislike;
    }

    private Duration resolveBaseDuration(String window) {
        return switch (window) {
            case "7d" -> Duration.ofDays(7);
            case "30d" -> Duration.ofDays(30);
            default -> Duration.ofHours(24);
        };
    }

    private List<ScoredArticle> scoreEligibleInRange(
            LocalDateTime startInclusive,
            LocalDateTime end,
            boolean endInclusive,
            int minLikes,
            int minViews
    ) {
        Condition publishedAtCond = endInclusive
                ? ARTICLE.PUBLISHED_AT.ge(startInclusive).and(ARTICLE.PUBLISHED_AT.le(end))
                : ARTICLE.PUBLISHED_AT.ge(startInclusive).and(ARTICLE.PUBLISHED_AT.lt(end));

        var records = dsl.select(
                        ARTICLE.ID,
                        ARTICLE.TITLE,
                        ARTICLE.CONTENT,
                        ARTICLE.COVER_URL,
                        ARTICLE.VIEW_COUNT,
                        ARTICLE.LIKE_COUNT,
                        ARTICLE.COMMENT_COUNT,
                        ARTICLE.DISLIKE_COUNT,
                        ARTICLE.PUBLISHED_AT,
                        ARTICLE.USER_ID,
                        USER.NICKNAME,
                        USER.AVATAR_URL
                )
                .from(ARTICLE)
                .join(USER).on(USER.ID.eq(ARTICLE.USER_ID))
                .where(ARTICLE.STATUS.eq(ArticleStatus.PUBLISHED.code()))
                .and(publishedAtCond)
                .fetch();

        List<ScoredArticle> scored = new ArrayList<>();
        for (var record : records) {
            int likes = record.get(ARTICLE.LIKE_COUNT) == null ? 0 : record.get(ARTICLE.LIKE_COUNT);
            int views = record.get(ARTICLE.VIEW_COUNT) == null ? 0 : record.get(ARTICLE.VIEW_COUNT);
            int comments = record.get(ARTICLE.COMMENT_COUNT) == null ? 0 : record.get(ARTICLE.COMMENT_COUNT);
            if (likes < minLikes && views < minViews) {
                continue;
            }
            double hotScore = calculateHotScore(
                    views,
                    likes,
                    comments,
                    record.get(ARTICLE.DISLIKE_COUNT) == null ? 0 : record.get(ARTICLE.DISLIKE_COUNT)
            );
            if (hotScore <= 0) {
                continue;
            }
            LocalDateTime publishedAt = record.get(ARTICLE.PUBLISHED_AT);
            scored.add(new ScoredArticle(
                    record.get(ARTICLE.ID),
                    record.get(ARTICLE.TITLE),
                    record.get(ARTICLE.USER_ID),
                    record.get(USER.NICKNAME),
                    record.get(USER.AVATAR_URL),
                    record.get(ARTICLE.COVER_URL),
                    ContentSummary.summarize(record.get(ARTICLE.CONTENT), 60),
                    views,
                    likes,
                    comments,
                    publishedAt == null ? null : publishedAt.atOffset(ZoneOffset.UTC).toInstant().toString(),
                    hotScore
            ));
        }
        return scored;
    }

    private boolean hasEligibleBefore(LocalDateTime before, int minLikes, int minViews) {
        return dsl.fetchExists(
                dsl.selectOne()
                        .from(ARTICLE)
                        .where(ARTICLE.STATUS.eq(ArticleStatus.PUBLISHED.code()))
                        .and(ARTICLE.PUBLISHED_AT.lt(before))
                        .and(ARTICLE.PUBLISHED_AT.isNotNull())
                        .and(ARTICLE.LIKE_COUNT.ge(minLikes).or(ARTICLE.VIEW_COUNT.ge(minViews)))
        );
    }

    private static HotRankingItemDto toItem(ScoredArticle article, int rank) {
        return new HotRankingItemDto(
                article.id(),
                article.title(),
                article.authorId(),
                article.authorNickname(),
                article.authorAvatarUrl(),
                article.coverUrl(),
                article.summary(),
                article.viewCount(),
                article.likeCount(),
                article.commentCount(),
                article.publishedAt(),
                Math.round(article.hotScore() * 10.0) / 10.0,
                rank
        );
    }

    private record ScoredArticle(
            long id,
            String title,
            Long authorId,
            String authorNickname,
            String authorAvatarUrl,
            String coverUrl,
            String summary,
            int viewCount,
            int likeCount,
            int commentCount,
            String publishedAt,
            double hotScore
    ) {
    }
}
