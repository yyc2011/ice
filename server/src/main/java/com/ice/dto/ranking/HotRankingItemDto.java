package com.ice.dto.ranking;

public record HotRankingItemDto(
        long id,
        String title,
        Long author_id,
        String author_nickname,
        String author_avatar_url,
        String cover_url,
        String summary,
        int view_count,
        int like_count,
        int comment_count,
        String published_at,
        double hot_score,
        int rank
) {
}
