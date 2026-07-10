package com.ice.dto.auth;

public record LoginResponse(
        String token,
        long expires_in,
        UserSummary user
) {
}
