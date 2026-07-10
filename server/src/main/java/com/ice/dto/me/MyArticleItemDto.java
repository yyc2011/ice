package com.ice.dto.me;

public record MyArticleItemDto(
        long id,
        String title,
        String status,
        String cover_url,
        int word_count,
        String created_at,
        String updated_at
) {
}
