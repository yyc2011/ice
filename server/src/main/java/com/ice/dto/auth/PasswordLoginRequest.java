package com.ice.dto.auth;

public record PasswordLoginRequest(
        String account_name,
        String password
) {
}
