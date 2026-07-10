package com.ice.dto.me;

public record ChangePasswordRequest(
        String account_name,
        String old_password,
        String new_password,
        String confirm_password
) {
}
