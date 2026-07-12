package com.ice.util;

public final class ContentSummary {

    private ContentSummary() {
    }

    public static String summarize(String content, int maxChars) {
        if (content == null || content.isBlank()) {
            return "";
        }
        String normalized = content.replaceAll("\\s+", " ").trim();
        if (normalized.length() <= maxChars) {
            return normalized;
        }
        return normalized.substring(0, maxChars) + "…";
    }
}
