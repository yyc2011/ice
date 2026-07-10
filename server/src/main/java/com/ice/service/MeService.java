package com.ice.service;

import com.ice.domain.ArticleStatus;
import com.ice.domain.UserLevel;
import com.ice.domain.UserRole;
import com.ice.dto.me.MeResponse;
import com.ice.dto.me.MyArticleItem;
import com.ice.dto.me.MyArticlesResponse;
import com.ice.exception.ApiException;
import com.ice.generated.jooq.tables.records.UserRecord;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.List;
import org.jooq.DSLContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.ice.generated.jooq.tables.Article.ARTICLE;
import static com.ice.generated.jooq.tables.User.USER;

@Service
public class MeService {

    private final DSLContext dsl;

    public MeService(DSLContext dsl) {
        this.dsl = dsl;
    }

    public MeResponse getMe(long userId) {
        UserRecord user = dsl.selectFrom(USER).where(USER.ID.eq(userId)).fetchOne();
        if (user == null) {
            throw new ApiException("UNAUTHORIZED", "用户不存在", HttpStatus.UNAUTHORIZED);
        }
        UserLevel level = UserLevel.fromCode(
                user.getLevel() == null ? UserLevel.READER.code() : user.getLevel().byteValue()
        );
        UserRole role = UserRole.fromCode(user.getRole() == null ? UserRole.USER.code() : user.getRole());
        return new MeResponse(
                user.getId(),
                user.getNickname(),
                user.getAvatarUrl(),
                level.apiValue(),
                level.displayName(),
                user.getBookCoinBalance() == null ? 0 : user.getBookCoinBalance(),
                user.getFollowerCount() == null ? 0 : user.getFollowerCount(),
                user.getFollowingCount() == null ? 0 : user.getFollowingCount(),
                user.getArticleCount() == null ? 0 : user.getArticleCount(),
                role.apiValue(),
                level.freeWordLimit()
        );
    }

    public MyArticlesResponse listArticles(long userId, String status, int page, int size) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.min(Math.max(size, 1), 50);
        int offset = (safePage - 1) * safeSize;

        var condition = ARTICLE.USER_ID.eq(userId);
        if (status != null && !status.isBlank()) {
            ArticleStatus articleStatus = ArticleStatus.fromApiValue(status.trim());
            condition = condition.and(ARTICLE.STATUS.eq(articleStatus.code()));
        }

        long total = dsl.fetchCount(dsl.selectFrom(ARTICLE).where(condition));
        List<MyArticleItem> items = dsl.select(ARTICLE.ID, ARTICLE.TITLE, ARTICLE.STATUS, ARTICLE.UPDATED_AT)
                .from(ARTICLE)
                .where(condition)
                .orderBy(ARTICLE.UPDATED_AT.desc())
                .limit(safeSize)
                .offset(offset)
                .fetch(record -> new MyArticleItem(
                        record.get(ARTICLE.ID),
                        record.get(ARTICLE.TITLE),
                        ArticleStatus.fromCode(record.get(ARTICLE.STATUS)).apiValue(),
                        record.get(ARTICLE.UPDATED_AT).atOffset(ZoneOffset.UTC).toInstant()
                ));

        return new MyArticlesResponse(items, total, safePage, safeSize);
    }
}
