package com.ice.dto.topic;

public record TopicDetailResponse(
        long id,
        String title,
        String description,
        String cover_url,
        String status,
        int article_count,
        int view_count,
        int like_count,
        int duration_days,
        int reward_pool_amount,
        String start_at,
        String end_at,
        String creator_nickname,
        Integer days_remaining
) {
}
