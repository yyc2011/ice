package com.ice.client;

import com.ice.client.dto.AiAuditRequest;
import com.ice.client.dto.AiAuditResponse;
import com.ice.config.AiProperties;
import java.time.Duration;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Component
public class AiAuditClient {

    private static final Logger log = LoggerFactory.getLogger(AiAuditClient.class);

    private final RestClient restClient;
    private final AiProperties properties;

    public AiAuditClient(AiProperties properties) {
        this.properties = properties;
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofMillis(properties.getTimeoutMs()));
        factory.setReadTimeout(Duration.ofMillis(properties.getTimeoutMs()));
        this.restClient = RestClient.builder()
                .requestFactory(factory)
                .baseUrl(properties.getBaseUrl())
                .build();
    }

    public AiAuditResponse audit(
            String requestId,
            String reviewType,
            String title,
            String body,
            List<String> tags
    ) {
        AiAuditRequest request = new AiAuditRequest(
                requestId,
                "article",
                reviewType,
                title,
                body,
                tags,
                null,
                null
        );
        try {
            return restClient.post()
                    .uri("/internal/audit")
                    .header("X-Internal-Token", properties.getInternalToken())
                    .body(request)
                    .retrieve()
                    .body(AiAuditResponse.class);
        } catch (RestClientException ex) {
            log.warn("AI audit call failed: {}", ex.getMessage());
            return null;
        }
    }

    public AiAuditResponse auditTopic(
            String requestId,
            String reviewType,
            String title,
            String description
    ) {
        AiAuditRequest request = new AiAuditRequest(
                requestId,
                "topic",
                reviewType,
                title,
                description,
                null,
                title,
                null
        );
        try {
            return restClient.post()
                    .uri("/internal/audit")
                    .header("X-Internal-Token", properties.getInternalToken())
                    .body(request)
                    .retrieve()
                    .body(AiAuditResponse.class);
        } catch (RestClientException ex) {
            log.warn("AI topic audit call failed: {}", ex.getMessage());
            return null;
        }
    }
}
