package com.ice.dto.article;

public record FeaturedArticleItemDto(
        long id,
        String title,
        String cover_url,
        String author_nickname,
        String category_name,
        double featured_score,
        String published_at
) {
}
