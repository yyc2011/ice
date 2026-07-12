package com.ice.service;

import com.ice.domain.ArticleStatus;
import com.ice.domain.TopicStatus;
import com.ice.domain.UserLevel;
import com.ice.dto.search.SearchArticleItemDto;
import com.ice.dto.search.SearchResponse;
import com.ice.dto.search.SearchUserItemDto;
import com.ice.dto.topic.TopicItemDto;
import com.ice.exception.ApiException;
import com.ice.generated.jooq.tables.records.TopicRecord;
import com.ice.util.ContentSummary;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import org.jooq.DSLContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.ice.generated.jooq.tables.Article.ARTICLE;
import static com.ice.generated.jooq.tables.ArticleTag.ARTICLE_TAG;
import static com.ice.generated.jooq.tables.Tag.TAG;
import static com.ice.generated.jooq.tables.Topic.TOPIC;
import static com.ice.generated.jooq.tables.User.USER;

@Service
public class SearchService {

    private final DSLContext dsl;

    public SearchService(DSLContext dsl) {
        this.dsl = dsl;
    }

    public SearchResponse search(String q, String type, int page, int size) {
        String keyword = q == null ? "" : q.trim();
        if (keyword.isEmpty()) {
            throw new ApiException("VALIDATION_ERROR", "请输入搜索关键词", HttpStatus.BAD_REQUEST);
        }
        String resolvedType = type == null || type.isBlank() ? "title" : type.trim().toLowerCase(Locale.ROOT);
        int safePage = Math.max(page, 1);
        int safeSize = Math.min(Math.max(size, 1), 50);
        String like = "%" + keyword + "%";

        return switch (resolvedType) {
            case "author" -> searchAuthors(keyword, like, safePage, safeSize);
            case "tag" -> searchByTag(like, safePage, safeSize, keyword, resolvedType);
            case "topic" -> searchTopics(keyword, like, safePage, safeSize);
            case "title" -> searchByTitle(like, safePage, safeSize, keyword, resolvedType);
            default -> throw new ApiException(
                    "VALIDATION_ERROR",
                    "type 须为 title|author|tag|topic",
                    HttpStatus.BAD_REQUEST
            );
        };
    }

    private SearchResponse searchByTitle(String like, int page, int size, String q, String type) {
        var all = dsl.select(
                        ARTICLE.ID,
                        ARTICLE.TITLE,
                        ARTICLE.CONTENT,
                        ARTICLE.COVER_URL,
                        ARTICLE.VIEW_COUNT,
                        ARTICLE.PUBLISHED_AT,
                        ARTICLE.USER_ID,
                        USER.NICKNAME,
                        USER.AVATAR_URL,
                        USER.LEVEL
                )
                .from(ARTICLE)
                .join(USER).on(USER.ID.eq(ARTICLE.USER_ID))
                .where(ARTICLE.STATUS.eq(ArticleStatus.PUBLISHED.code()))
                .and(ARTICLE.TITLE.likeIgnoreCase(like))
                .orderBy(ARTICLE.PUBLISHED_AT.desc())
                .fetch();

        List<SearchArticleItemDto> items = all.stream().map(this::toArticleItem).toList();
        return paginateArticles(type, q, items, page, size);
    }

    private SearchResponse searchByTag(String like, int page, int size, String q, String type) {
        var all = dsl.selectDistinct(
                        ARTICLE.ID,
                        ARTICLE.TITLE,
                        ARTICLE.CONTENT,
                        ARTICLE.COVER_URL,
                        ARTICLE.VIEW_COUNT,
                        ARTICLE.PUBLISHED_AT,
                        ARTICLE.USER_ID,
                        USER.NICKNAME,
                        USER.AVATAR_URL,
                        USER.LEVEL
                )
                .from(ARTICLE)
                .join(USER).on(USER.ID.eq(ARTICLE.USER_ID))
                .join(ARTICLE_TAG).on(ARTICLE_TAG.ARTICLE_ID.eq(ARTICLE.ID))
                .join(TAG).on(TAG.ID.eq(ARTICLE_TAG.TAG_ID))
                .where(ARTICLE.STATUS.eq(ArticleStatus.PUBLISHED.code()))
                .and(TAG.NAME.likeIgnoreCase(like))
                .orderBy(ARTICLE.PUBLISHED_AT.desc())
                .fetch();

        List<SearchArticleItemDto> items = all.stream().map(this::toArticleItem).toList();
        return paginateArticles(type, q, items, page, size);
    }

    private SearchResponse searchAuthors(String keyword, String like, int page, int size) {
        var records = dsl.selectFrom(USER)
                .where(USER.NICKNAME.likeIgnoreCase(like))
                .fetch();

        String lowerKeyword = keyword.toLowerCase(Locale.ROOT);
        List<SearchUserItemDto> users = new ArrayList<>();
        for (var user : records) {
            users.add(new SearchUserItemDto(
                    user.getId(),
                    user.getNickname(),
                    user.getAvatarUrl(),
                    user.getBio(),
                    user.getArticleCount() == null ? 0 : user.getArticleCount(),
                    user.getTotalLikesReceived() == null ? 0 : user.getTotalLikesReceived()
            ));
        }
        users.sort(Comparator
                .comparing((SearchUserItemDto u) -> {
                    String nick = u.nickname() == null ? "" : u.nickname().toLowerCase(Locale.ROOT);
                    if (nick.equals(lowerKeyword)) {
                        return 0;
                    }
                    if (nick.startsWith(lowerKeyword)) {
                        return 1;
                    }
                    return 2;
                })
                .thenComparing(SearchUserItemDto::article_count, Comparator.reverseOrder()));

        int total = users.size();
        int from = (page - 1) * size;
        List<SearchUserItemDto> pageItems = from >= total
                ? List.of()
                : users.subList(from, Math.min(from + size, total));
        return new SearchResponse("author", keyword, total, page, size, List.of(), pageItems, List.of());
    }

    private SearchResponse searchTopics(String keyword, String like, int page, int size) {
        LocalDateTime now = LocalDateTime.now();
        List<TopicRecord> records = dsl.selectFrom(TOPIC)
                .where(TOPIC.TITLE.likeIgnoreCase(like))
                .and(TOPIC.STATUS.in(
                        TopicStatus.ONGOING.code(),
                        TopicStatus.ENDED.code(),
                        TopicStatus.REVIEWING.code()
                ))
                .fetch();

        List<TopicItemDto> topics = new ArrayList<>();
        for (TopicRecord topic : records) {
            TopicStatus status = TopicStatus.fromCode(topic.getStatus());
            // upcoming = reviewing with future start, or status upcoming mapping
            topics.add(toTopicItem(topic, status, now));
        }
        topics.sort(Comparator
                .comparing((TopicItemDto t) -> "ongoing".equals(t.status()) ? 0 : 1)
                .thenComparing(TopicItemDto::article_count, Comparator.reverseOrder()));

        int total = topics.size();
        int from = (page - 1) * size;
        List<TopicItemDto> pageItems = from >= total
                ? List.of()
                : topics.subList(from, Math.min(from + size, total));
        return new SearchResponse("topic", keyword, total, page, size, List.of(), List.of(), pageItems);
    }

    private SearchResponse paginateArticles(
            String type,
            String q,
            List<SearchArticleItemDto> items,
            int page,
            int size
    ) {
        int total = items.size();
        int from = (page - 1) * size;
        List<SearchArticleItemDto> pageItems = from >= total
                ? List.of()
                : items.subList(from, Math.min(from + size, total));
        return new SearchResponse(type, q, total, page, size, pageItems, List.of(), List.of());
    }

    private SearchArticleItemDto toArticleItem(org.jooq.Record record) {
        byte levelCode = record.get(USER.LEVEL) == null
                ? UserLevel.READER.code()
                : record.get(USER.LEVEL).byteValue();
        LocalDateTime publishedAt = record.get(ARTICLE.PUBLISHED_AT);
        return new SearchArticleItemDto(
                record.get(ARTICLE.ID),
                record.get(ARTICLE.TITLE),
                record.get(ARTICLE.COVER_URL),
                ContentSummary.summarize(record.get(ARTICLE.CONTENT), 60),
                record.get(ARTICLE.USER_ID),
                record.get(USER.NICKNAME),
                record.get(USER.AVATAR_URL),
                UserLevel.fromCode(levelCode).displayName(),
                record.get(ARTICLE.VIEW_COUNT) == null ? 0 : record.get(ARTICLE.VIEW_COUNT),
                publishedAt == null ? null : publishedAt.atOffset(ZoneOffset.UTC).toInstant().toString()
        );
    }

    private TopicItemDto toTopicItem(TopicRecord topic, TopicStatus status, LocalDateTime now) {
        Integer daysRemaining = null;
        if (topic.getEndAt() != null) {
            daysRemaining = (int) Math.max(ChronoUnit.DAYS.between(now, topic.getEndAt()), 0);
        }
        String apiStatus = status.apiValue();
        if (status == TopicStatus.ONGOING
                && topic.getStartAt() != null
                && topic.getStartAt().isAfter(now)) {
            apiStatus = "upcoming";
        }
        if (status == TopicStatus.REVIEWING) {
            apiStatus = "upcoming";
        }
        return new TopicItemDto(
                topic.getId(),
                topic.getTitle(),
                topic.getDescription(),
                topic.getCoverUrl(),
                apiStatus,
                topic.getArticleCount() == null ? 0 : topic.getArticleCount(),
                topic.getViewCount() == null ? 0 : topic.getViewCount(),
                topic.getStartAt() == null ? null : topic.getStartAt().atOffset(ZoneOffset.UTC).toInstant().toString(),
                topic.getEndAt() == null ? null : topic.getEndAt().atOffset(ZoneOffset.UTC).toInstant().toString(),
                daysRemaining
        );
    }
}
