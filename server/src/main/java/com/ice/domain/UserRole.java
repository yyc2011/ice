package com.ice.domain;

import java.util.Set;

public enum UserRole {
    SUPER_ADMIN((byte) 0, "super_admin", "超级管理员"),
    ADMIN((byte) 1, "admin", "管理员"),
    USER((byte) 2, "user", "普通用户");

    private static final Set<UserRole> ADMIN_ACCESS = Set.of(SUPER_ADMIN, ADMIN);

    private final byte code;
    private final String apiValue;
    private final String displayName;

    UserRole(int code, String apiValue, String displayName) {
        this.code = (byte) code;
        this.apiValue = apiValue;
        this.displayName = displayName;
    }

    public byte code() {
        return code;
    }

    public String apiValue() {
        return apiValue;
    }

    public String displayName() {
        return displayName;
    }

    public boolean canAccessAdmin() {
        return ADMIN_ACCESS.contains(this);
    }

    public static boolean canAccessAdmin(byte code) {
        return ADMIN_ACCESS.contains(fromCode(code));
    }

    public boolean canManageAdmins() {
        return this == SUPER_ADMIN;
    }

    public static UserRole fromCode(byte code) {
        for (UserRole role : values()) {
            if (role.code == code) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown user role: " + code);
    }

    public static UserRole fromApiValue(String apiValue) {
        if (apiValue == null) {
            throw new IllegalArgumentException("role is required");
        }
        for (UserRole role : values()) {
            if (role.apiValue.equals(apiValue)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown user role: " + apiValue);
    }
}
