package com.ice.controller.admin;

import com.ice.auth.AuthContext;
import com.ice.dto.admin.ReviewDetailDto;
import com.ice.dto.admin.ReviewListResponse;
import com.ice.dto.admin.ReviewRejectRequest;
import com.ice.service.admin.AdminReviewService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin/reviews")
public class AdminReviewController {

    private final AdminReviewService adminReviewService;

    public AdminReviewController(AdminReviewService adminReviewService) {
        this.adminReviewService = adminReviewService;
    }

    @GetMapping
    public ReviewListResponse list(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return adminReviewService.list(page, size);
    }

    @GetMapping("/{id}")
    public ReviewDetailDto get(@PathVariable long id) {
        return adminReviewService.get(id);
    }

    @PostMapping("/{id}/approve")
    public void approve(@PathVariable long id) {
        adminReviewService.approve(id, AuthContext.requireUserId());
    }

    @PostMapping("/{id}/reject")
    public void reject(@PathVariable long id, @RequestBody ReviewRejectRequest request) {
        adminReviewService.reject(id, AuthContext.requireUserId(), request);
    }
}
