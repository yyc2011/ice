package com.ice.controller;

import com.ice.auth.AuthContext;
import com.ice.dto.notification.MarkNotificationsReadRequest;
import com.ice.dto.notification.NotificationListResponse;
import com.ice.service.NotificationQueryService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/messages")
public class NotificationController {

    private final NotificationQueryService notificationQueryService;

    public NotificationController(NotificationQueryService notificationQueryService) {
        this.notificationQueryService = notificationQueryService;
    }

    @GetMapping("/notifications")
    public NotificationListResponse listNotifications(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return notificationQueryService.listForUser(AuthContext.requireUserId(), page, size);
    }

    @PostMapping("/notifications/read")
    public void markRead(@RequestBody(required = false) MarkNotificationsReadRequest request) {
        var ids = request == null ? null : request.ids();
        notificationQueryService.markRead(AuthContext.requireUserId(), ids);
    }
}
