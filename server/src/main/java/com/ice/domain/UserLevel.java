package com.ice.domain;

import java.util.Set;

public enum UserLevel {
    READER((byte) 0, "reader", "读者"),
    SPROUT((byte) 1, "sprout", "新芽"),
    GROWTH((byte) 2, "growth", "成长"),
    CREATOR((byte) 3, "creator", "创作者"),
    SEASONED((byte) 4, "seasoned", "深耕"),
    MASTER((byte) 5, "master", "妙笔");

    private static final Set<UserLevel> TOPIC_CREATE_ALLOWED =
            Set.of(GROWTH, CREATOR, SEASONED, MASTER);

    private final byte code;
    private final String apiValue;
    private final String displayName;

    UserLevel(int code, String apiValue, String displayName) {
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

    public static boolean canCreateTopic(byte levelCode) {
        return TOPIC_CREATE_ALLOWED.contains(fromCode(levelCode));
    }

    public static UserLevel fromCode(byte code) {
        for (UserLevel level : values()) {
            if (level.code == code) {
                return level;
            }
        }
        throw new IllegalArgumentException("Unknown user level: " + code);
    }

    public int freeWordLimit() {
        return switch (this) {
            case READER, SPROUT -> 800;
            case GROWTH -> 1500;
            case CREATOR -> 3000;
            case SEASONED -> 4000;
            case MASTER -> 5000;
        };
    }

    public static UserLevel fromApiValue(String apiValue) {
        if (apiValue == null) {
            throw new IllegalArgumentException("level is required");
        }
        for (UserLevel level : values()) {
            if (level.apiValue.equals(apiValue)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Unknown user level: " + apiValue);
    }
}
