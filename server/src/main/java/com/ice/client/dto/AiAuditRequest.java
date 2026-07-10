package com.ice.client.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record AiAuditRequest(
        String request_id,
        String content_type,
        String review_type,
        String title,
        String body,
        List<String> tags,
        String topic_title,
        String appeal_text
) {
}
