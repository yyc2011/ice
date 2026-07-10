package com.ice.dto.config;

public record ClientConfigResponse(
        boolean recharge_enabled,
        boolean recharge_custom_amount_enabled
) {
}
