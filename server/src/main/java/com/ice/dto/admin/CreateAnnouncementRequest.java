package com.ice.dto.admin;

public record CreateAnnouncementRequest(String title, String content, Boolean publish_now) {
}
