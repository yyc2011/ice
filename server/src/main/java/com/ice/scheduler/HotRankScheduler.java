package com.ice.scheduler;

import com.ice.dto.ranking.HotRankingItemDto;
import com.ice.dto.ranking.HotRankingResponse;
import com.ice.service.BookCoinService;
import com.ice.service.RankingService;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.ice.generated.jooq.tables.HotrankDaily.HOTRANK_DAILY;

@Component
public class HotRankScheduler {

    private static final Logger log = LoggerFactory.getLogger(HotRankScheduler.class);
    private static final int[] HOTRANK_REWARDS = {50, 40, 30, 25, 20, 15, 12, 10, 8, 5};

    private final RankingService rankingService;
    private final BookCoinService bookCoinService;
    private final DSLContext dsl;

    public HotRankScheduler(RankingService rankingService, BookCoinService bookCoinService, DSLContext dsl) {
        this.rankingService = rankingService;
        this.bookCoinService = bookCoinService;
        this.dsl = dsl;
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void settleDailyHotRank() {
        LocalDate today = LocalDate.now();
        int existing = dsl.fetchCount(
                dsl.selectFrom(HOTRANK_DAILY).where(HOTRANK_DAILY.RANK_DATE.eq(today))
        );
        if (existing > 0) {
            log.info("Hot rank already settled for {}", today);
            return;
        }

        HotRankingResponse ranking = rankingService.getHotRanking("24h", 10, false);
        int rank = 1;
        for (HotRankingItemDto item : ranking.items()) {
            int reward = rank <= HOTRANK_REWARDS.length ? HOTRANK_REWARDS[rank - 1] : 0;
            dsl.insertInto(HOTRANK_DAILY)
                    .set(HOTRANK_DAILY.RANK_DATE, today)
                    .set(HOTRANK_DAILY.ARTICLE_ID, item.id())
                    .set(HOTRANK_DAILY.RANK, (byte) rank)
                    .set(HOTRANK_DAILY.HOT_SCORE, BigDecimal.valueOf(item.hot_score()))
                    .set(HOTRANK_DAILY.REWARD_COIN, reward)
                    .set(HOTRANK_DAILY.CREATED_AT, LocalDateTime.now())
                    .execute();
            if (reward > 0) {
                bookCoinService.refund(
                        resolveAuthorId(item.id()),
                        reward,
                        "hotrank_reward",
                        "article",
                        item.id(),
                        "24小时热榜第" + rank + "名奖励"
                );
            }
            rank++;
        }
        log.info("Settled daily hot rank for {} with {} articles", today, ranking.items().size());
    }

    private long resolveAuthorId(long articleId) {
        Long userId = dsl.select(com.ice.generated.jooq.tables.Article.ARTICLE.USER_ID)
                .from(com.ice.generated.jooq.tables.Article.ARTICLE)
                .where(com.ice.generated.jooq.tables.Article.ARTICLE.ID.eq(articleId))
                .fetchOne(com.ice.generated.jooq.tables.Article.ARTICLE.USER_ID);
        return userId == null ? 0L : userId;
    }
}
