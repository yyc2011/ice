package com.ice.dto.ranking;

import java.util.List;

public record HotRankingResponse(
        String window,
        List<HotRankingItemDto> items
) {
}
