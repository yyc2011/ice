package com.ice.dto.notification;

import java.util.List;

public record MarkNotificationsReadRequest(List<Long> ids) {
}
