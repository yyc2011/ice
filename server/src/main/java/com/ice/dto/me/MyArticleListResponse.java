package com.ice.dto.me;

import java.util.List;

public record MyArticleListResponse(
        List<MyArticleItemDto> items,
        long total,
        int page,
        int size
) {
}
