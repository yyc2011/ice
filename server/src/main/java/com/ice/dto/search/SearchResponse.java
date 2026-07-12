package com.ice.dto.search;

import com.ice.dto.topic.TopicItemDto;
import java.util.List;

public record SearchResponse(
        String type,
        String q,
        int total,
        int page,
        int size,
        List<SearchArticleItemDto> articles,
        List<SearchUserItemDto> users,
        List<TopicItemDto> topics
) {
}
