package com.ice.controller.admin;

import com.ice.auth.AuthContext;
import com.ice.dto.admin.AnnouncementDto;
import com.ice.dto.admin.CreateAnnouncementRequest;
import com.ice.service.admin.AdminAnnouncementService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/announcements")
public class AdminAnnouncementController {

    private final AdminAnnouncementService adminAnnouncementService;

    public AdminAnnouncementController(AdminAnnouncementService adminAnnouncementService) {
        this.adminAnnouncementService = adminAnnouncementService;
    }

    @GetMapping
    public List<AnnouncementDto> list() {
        return adminAnnouncementService.list();
    }

    @PostMapping
    public AnnouncementDto create(@RequestBody CreateAnnouncementRequest request) {
        return adminAnnouncementService.create(AuthContext.requireUserId(), request);
    }
}
