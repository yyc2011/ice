package com.ice.dto.notification;

import java.util.List;

public record NotificationListResponse(List<NotificationItemDto> items, long total, int page, int size) {
}
