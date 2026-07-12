package com.ice.dto.article;

public record FeaturedArticleItemDto(
        long id,
        String title,
        String cover_url,
        String summary,
        Long author_id,
        String author_nickname,
        String author_avatar_url,
        String author_level_name,
        String category_name,
        int view_count,
        double featured_score,
        String published_at
) {
}
