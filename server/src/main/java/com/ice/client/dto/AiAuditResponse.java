package com.ice.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public record AiAuditResponse(
        String request_id,
        String result,
        int overall_score,
        Map<String, Integer> dimensions,
        String reject_reason,
        String model,
        int latency_ms
) {
}
