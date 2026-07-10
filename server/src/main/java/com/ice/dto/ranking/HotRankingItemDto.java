package com.ice.dto.ranking;

public record HotRankingItemDto(
        long id,
        String title,
        String author_nickname,
        int view_count,
        int like_count,
        double hot_score,
        int rank
) {
}
