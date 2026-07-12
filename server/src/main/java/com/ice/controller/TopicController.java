package com.ice.controller;

import com.ice.auth.AuthContext;
import com.ice.dto.topic.CreateTopicRequest;
import com.ice.dto.topic.CreateTopicResponse;
import com.ice.dto.topic.TopicArticlesResponse;
import com.ice.dto.topic.TopicDetailResponse;
import com.ice.dto.topic.TopicListResponse;
import com.ice.service.TopicService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/topics")
public class TopicController {

    private final TopicService topicService;

    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }

    @GetMapping("/ongoing")
    public TopicListResponse ongoing(@RequestParam(defaultValue = "5") int size) {
        return topicService.listOngoing(Math.min(Math.max(size, 1), 20));
    }

    @GetMapping
    public TopicListResponse list(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String period,
            @RequestParam(required = false) String mode
    ) {
        int limit = Math.min(Math.max(size, 1), 50);
        if ("random".equals(mode)) {
            return topicService.listRandomEnded(limit);
        }
        if ("historical".equals(mode)) {
            return topicService.listHistorical(period, limit);
        }
        if (status != null) {
            return topicService.list(status, limit);
        }
        return topicService.listOngoing(limit);
    }

    @GetMapping("/selectable")
    public TopicListResponse selectable() {
        return topicService.listSelectable();
    }

    @GetMapping("/{id}")
    public TopicDetailResponse detail(@PathVariable long id) {
        return topicService.getDetail(id);
    }

    @GetMapping("/{id}/articles")
    public TopicArticlesResponse articles(
            @PathVariable long id,
            @RequestParam(defaultValue = "hot") String sort,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return topicService.listTopicArticles(id, sort, page, Math.min(Math.max(size, 1), 50));
    }

    @PostMapping
    public CreateTopicResponse create(@RequestBody CreateTopicRequest request) {
        return topicService.createTopic(AuthContext.requireUserId(), request);
    }
}
