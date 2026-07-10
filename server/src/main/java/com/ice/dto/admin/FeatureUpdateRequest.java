package com.ice.dto.admin;

import java.util.List;

public record FeatureUpdateRequest(List<FeatureConfigDto> items) {
}
