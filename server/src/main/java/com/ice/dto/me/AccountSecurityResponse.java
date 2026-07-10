package com.ice.dto.me;

public record AccountSecurityResponse(
        String phone_masked,
        boolean phone_verified,
        boolean password_set,
        boolean wechat_bound,
        String account_name,
        boolean account_name_set,
        boolean can_change_account_name,
        String account_name_next_change_at
) {
}
