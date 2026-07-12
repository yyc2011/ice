package com.ice.dto.topic;

public record TopicArticleItemDto(
        long id,
        String title,
        String author_nickname,
        int view_count,
        int like_count,
        Integer rank,
        String published_at,
        double hot_score
) {
}
