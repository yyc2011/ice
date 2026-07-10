package com.ice.dto.me;

public record ChangeAccountNameRequest(
        String new_account_name,
        String password
) {
}
