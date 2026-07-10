package com.ice.dto.topic;

public record CreateTopicResponse(
        long id,
        String status,
        String message
) {
}
