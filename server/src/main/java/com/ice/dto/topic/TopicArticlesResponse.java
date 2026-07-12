package com.ice.dto.topic;

import java.util.List;

public record TopicArticlesResponse(
        List<TopicArticleItemDto> items,
        int total,
        int page,
        int size
) {
}
