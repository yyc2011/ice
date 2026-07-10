package com.ice.auth;

import com.ice.domain.UserRole;

public final class AuthContext {

    private static final ThreadLocal<Long> CURRENT_USER = new ThreadLocal<>();
    private static final ThreadLocal<UserRole> CURRENT_ROLE = new ThreadLocal<>();

    private AuthContext() {
    }

    public static void set(long userId, UserRole role) {
        CURRENT_USER.set(userId);
        CURRENT_ROLE.set(role);
    }

    public static Long getUserId() {
        return CURRENT_USER.get();
    }

    public static UserRole getRole() {
        return CURRENT_ROLE.get();
    }

    public static long requireUserId() {
        Long userId = CURRENT_USER.get();
        if (userId == null) {
            throw new UnauthorizedException("未登录");
        }
        return userId;
    }

    public static UserRole requireRole() {
        UserRole role = CURRENT_ROLE.get();
        if (role == null) {
            throw new UnauthorizedException("未登录");
        }
        return role;
    }

    public static void clear() {
        CURRENT_USER.remove();
        CURRENT_ROLE.remove();
    }
}
