package com.ice.dto.topic;

public record CreateTopicRequest(
        String title,
        String description,
        Integer duration_days,
        String cover_url,
        Integer reward_pool_amount,
        Integer reward_top_n,
        String reward_ratio
) {
}
