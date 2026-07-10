package com.ice.dto.category;

import java.util.List;

public record CategoryPreviewResponse(
        List<CategoryPreviewItemDto> categories
) {
}
