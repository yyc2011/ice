package com.ice.dto.topic;

import java.util.List;

public record TopicListResponse(
        List<TopicItemDto> items,
        int total
) {
}
