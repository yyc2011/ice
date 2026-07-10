package com.ice.controller.admin;

import com.ice.dto.admin.ReportDto;
import com.ice.dto.admin.ReportResolveRequest;
import com.ice.service.admin.AdminReportService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/reports")
public class AdminReportController {

    private final AdminReportService adminReportService;

    public AdminReportController(AdminReportService adminReportService) {
        this.adminReportService = adminReportService;
    }

    @GetMapping
    public List<ReportDto> list() {
        return adminReportService.list();
    }

    @PostMapping("/{id}/resolve")
    public void resolve(@PathVariable long id, @RequestBody ReportResolveRequest request) {
        adminReportService.resolve(id, request);
    }
}
