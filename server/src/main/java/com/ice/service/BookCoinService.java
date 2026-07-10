package com.ice.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ice.generated.jooq.tables.BookCoinConfig.BOOK_COIN_CONFIG;
import static com.ice.generated.jooq.tables.BookCoinLog.BOOK_COIN_LOG;
import static com.ice.generated.jooq.tables.User.USER;

@Service
public class BookCoinService {

    private final DSLContext dsl;

    public BookCoinService(DSLContext dsl) {
        this.dsl = dsl;
    }

    @Transactional
    public void rewardPublishIfEligible(long userId, long articleId) {
        int reward = getConfigInt("reward_publish_article", 5);
        int dailyLimit = getConfigInt("reward_publish_daily_limit", 3);
        LocalDateTime dayStart = LocalDate.now().atStartOfDay();
        int rewardedToday = dsl.fetchCount(
                dsl.selectFrom(BOOK_COIN_LOG)
                        .where(BOOK_COIN_LOG.USER_ID.eq(userId))
                        .and(BOOK_COIN_LOG.TYPE.eq("publish_reward"))
                        .and(BOOK_COIN_LOG.CREATED_AT.ge(dayStart))
        );
        if (rewardedToday >= dailyLimit) {
            return;
        }
        Integer balance = dsl.select(USER.BOOK_COIN_BALANCE)
                .from(USER)
                .where(USER.ID.eq(userId))
                .fetchOne(USER.BOOK_COIN_BALANCE);
        int current = balance == null ? 0 : balance;
        int next = current + reward;
        dsl.update(USER)
                .set(USER.BOOK_COIN_BALANCE, next)
                .where(USER.ID.eq(userId))
                .execute();
        LocalDateTime now = LocalDateTime.now();
        dsl.insertInto(BOOK_COIN_LOG)
                .set(BOOK_COIN_LOG.USER_ID, userId)
                .set(BOOK_COIN_LOG.AMOUNT, reward)
                .set(BOOK_COIN_LOG.BALANCE_AFTER, next)
                .set(BOOK_COIN_LOG.TYPE, "publish_reward")
                .set(BOOK_COIN_LOG.REF_TYPE, "article")
                .set(BOOK_COIN_LOG.REF_ID, articleId)
                .set(BOOK_COIN_LOG.REMARK, "发文审核通过奖励")
                .set(BOOK_COIN_LOG.CREATED_AT, now)
                .execute();
    }

    private int getConfigInt(String key, int defaultValue) {
        String value = dsl.select(BOOK_COIN_CONFIG.CONFIG_VALUE)
                .from(BOOK_COIN_CONFIG)
                .where(BOOK_COIN_CONFIG.CONFIG_KEY.eq(key))
                .fetchOne(BOOK_COIN_CONFIG.CONFIG_VALUE);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    @Transactional
    public void spend(long userId, int amount, String type, String refType, Long refId, String remark) {
        if (amount <= 0) {
            return;
        }
        Integer balance = dsl.select(USER.BOOK_COIN_BALANCE)
                .from(USER)
                .where(USER.ID.eq(userId))
                .fetchOne(USER.BOOK_COIN_BALANCE);
        int current = balance == null ? 0 : balance;
        if (current < amount) {
            throw new com.ice.exception.ApiException(
                    "INSUFFICIENT_COINS",
                    "书币不足",
                    org.springframework.http.HttpStatus.PAYMENT_REQUIRED
            );
        }
        int next = current - amount;
        dsl.update(USER)
                .set(USER.BOOK_COIN_BALANCE, next)
                .where(USER.ID.eq(userId))
                .execute();
        LocalDateTime now = LocalDateTime.now();
        dsl.insertInto(BOOK_COIN_LOG)
                .set(BOOK_COIN_LOG.USER_ID, userId)
                .set(BOOK_COIN_LOG.AMOUNT, -amount)
                .set(BOOK_COIN_LOG.BALANCE_AFTER, next)
                .set(BOOK_COIN_LOG.TYPE, type)
                .set(BOOK_COIN_LOG.REF_TYPE, refType)
                .set(BOOK_COIN_LOG.REF_ID, refId)
                .set(BOOK_COIN_LOG.REMARK, remark)
                .set(BOOK_COIN_LOG.CREATED_AT, now)
                .execute();
    }

    @Transactional
    public void refund(long userId, int amount, String type, String refType, Long refId, String remark) {
        if (amount <= 0) {
            return;
        }
        Integer balance = dsl.select(USER.BOOK_COIN_BALANCE)
                .from(USER)
                .where(USER.ID.eq(userId))
                .fetchOne(USER.BOOK_COIN_BALANCE);
        int current = balance == null ? 0 : balance;
        int next = current + amount;
        dsl.update(USER)
                .set(USER.BOOK_COIN_BALANCE, next)
                .where(USER.ID.eq(userId))
                .execute();
        LocalDateTime now = LocalDateTime.now();
        dsl.insertInto(BOOK_COIN_LOG)
                .set(BOOK_COIN_LOG.USER_ID, userId)
                .set(BOOK_COIN_LOG.AMOUNT, amount)
                .set(BOOK_COIN_LOG.BALANCE_AFTER, next)
                .set(BOOK_COIN_LOG.TYPE, type)
                .set(BOOK_COIN_LOG.REF_TYPE, refType)
                .set(BOOK_COIN_LOG.REF_ID, refId)
                .set(BOOK_COIN_LOG.REMARK, remark)
                .set(BOOK_COIN_LOG.CREATED_AT, now)
                .execute();
    }
}
