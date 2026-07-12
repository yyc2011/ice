package com.ice.controller;

import com.ice.dto.ranking.HotRankingResponse;
import com.ice.service.RankingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rankings")
public class RankingController {

    private final RankingService rankingService;

    public RankingController(RankingService rankingService) {
        this.rankingService = rankingService;
    }

    @GetMapping("/hot")
    public HotRankingResponse hot(
            @RequestParam(defaultValue = "24h") String window,
            @RequestParam(defaultValue = "10") int size
    ) {
        int limit = Math.min(Math.max(size, 1), 50);
        return rankingService.getHotRanking(window, limit, true);
    }
}
