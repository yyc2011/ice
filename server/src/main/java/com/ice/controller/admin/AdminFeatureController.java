package com.ice.controller.admin;

import com.ice.dto.admin.FeatureConfigDto;
import com.ice.dto.admin.FeatureUpdateRequest;
import com.ice.service.admin.AdminFeatureService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/features")
public class AdminFeatureController {

    private final AdminFeatureService adminFeatureService;

    public AdminFeatureController(AdminFeatureService adminFeatureService) {
        this.adminFeatureService = adminFeatureService;
    }

    @GetMapping
    public List<FeatureConfigDto> list() {
        return adminFeatureService.list();
    }

    @PostMapping
    public void update(@RequestBody FeatureUpdateRequest request) {
        adminFeatureService.update(request);
    }
}
