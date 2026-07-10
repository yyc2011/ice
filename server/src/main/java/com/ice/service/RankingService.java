package com.ice.service;

import com.ice.domain.ArticleStatus;
import com.ice.dto.ranking.HotRankingItemDto;
import com.ice.dto.ranking.HotRankingResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
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

    public HotRankingResponse getHotRanking(String window, int limit) {
        String resolvedWindow = window == null || window.isBlank() ? "24h" : window.trim();
        LocalDateTime since = resolveSince(resolvedWindow);
        int minLikes = rankingConfigService.getInt("hot_min_likes", 1);
        int minViews = rankingConfigService.getInt("hot_min_views", 20);

        var records = dsl.select(
                        ARTICLE.ID,
                        ARTICLE.TITLE,
                        ARTICLE.VIEW_COUNT,
                        ARTICLE.LIKE_COUNT,
                        ARTICLE.COMMENT_COUNT,
                        ARTICLE.DISLIKE_COUNT,
                        USER.NICKNAME
                )
                .from(ARTICLE)
                .join(USER).on(USER.ID.eq(ARTICLE.USER_ID))
                .where(ARTICLE.STATUS.eq(ArticleStatus.PUBLISHED.code()))
                .and(ARTICLE.PUBLISHED_AT.ge(since))
                .fetch();

        List<ScoredArticle> scored = new ArrayList<>();
        for (var record : records) {
            int likes = record.get(ARTICLE.LIKE_COUNT) == null ? 0 : record.get(ARTICLE.LIKE_COUNT);
            int views = record.get(ARTICLE.VIEW_COUNT) == null ? 0 : record.get(ARTICLE.VIEW_COUNT);
            if (likes < minLikes && views < minViews) {
                continue;
            }
            double hotScore = calculateHotScore(
                    views,
                    likes,
                    record.get(ARTICLE.COMMENT_COUNT) == null ? 0 : record.get(ARTICLE.COMMENT_COUNT),
                    record.get(ARTICLE.DISLIKE_COUNT) == null ? 0 : record.get(ARTICLE.DISLIKE_COUNT)
            );
            if (hotScore <= 0) {
                continue;
            }
            scored.add(new ScoredArticle(
                    record.get(ARTICLE.ID),
                    record.get(ARTICLE.TITLE),
                    record.get(USER.NICKNAME),
                    views,
                    likes,
                    hotScore
            ));
        }

        scored.sort(Comparator.comparingDouble(ScoredArticle::hotScore).reversed());
        List<HotRankingItemDto> items = new ArrayList<>();
        int rank = 1;
        for (ScoredArticle article : scored) {
            if (items.size() >= limit) {
                break;
            }
            items.add(new HotRankingItemDto(
                    article.id(),
                    article.title(),
                    article.authorNickname(),
                    article.viewCount(),
                    article.likeCount(),
                    Math.round(article.hotScore() * 10.0) / 10.0,
                    rank++
            ));
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

    private LocalDateTime resolveSince(String window) {
        LocalDateTime now = LocalDateTime.now();
        return switch (window) {
            case "7d" -> now.minusDays(7);
            case "30d" -> now.minusDays(30);
            default -> now.minusHours(24);
        };
    }

    private record ScoredArticle(
            long id,
            String title,
            String authorNickname,
            int viewCount,
            int likeCount,
            double hotScore
    ) {
    }
}
