package com.ice.dto.me;

import java.util.List;

public record MyArticlesResponse(
        List<MyArticleItem> items,
        long total,
        int page,
        int size
) {
}
