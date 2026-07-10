package com.ice.dto.auth;

public record SmsCodeResponse(
        String message,
        int expires_in
) {
}
