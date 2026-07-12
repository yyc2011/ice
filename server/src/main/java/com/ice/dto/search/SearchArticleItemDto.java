package com.ice.dto.search;

public record SearchArticleItemDto(
        long id,
        String title,
        String cover_url,
        String summary,
        Long author_id,
        String author_nickname,
        String author_avatar_url,
        String author_level_name,
        int view_count,
        String published_at
) {
}
