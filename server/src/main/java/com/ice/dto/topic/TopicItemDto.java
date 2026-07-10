package com.ice.dto.topic;

public record TopicItemDto(
        long id,
        String title,
        String description,
        String cover_url,
        String status,
        int article_count,
        int view_count,
        String start_at,
        String end_at,
        Integer days_remaining
) {
}
