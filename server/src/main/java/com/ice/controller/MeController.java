package com.ice.controller;

import com.ice.auth.AuthContext;
import com.ice.dto.me.AccountSecurityResponse;
import com.ice.dto.me.BindPhoneRequest;
import com.ice.dto.me.ChangeAccountNameRequest;
import com.ice.dto.me.ChangePasswordRequest;
import com.ice.dto.me.MeResponse;
import com.ice.dto.me.MyArticlesResponse;
import com.ice.dto.me.WechatProfileRequest;
import com.ice.dto.auth.UserSummary;
import com.ice.dto.topic.TopicListResponse;
import com.ice.service.MeAccountService;
import com.ice.service.MeService;
import com.ice.service.TopicService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/me")
public class MeController {

    private final MeService meService;
    private final MeAccountService meAccountService;
    private final TopicService topicService;

    public MeController(MeService meService, MeAccountService meAccountService, TopicService topicService) {
        this.meService = meService;
        this.meAccountService = meAccountService;
        this.topicService = topicService;
    }

    @GetMapping
    public MeResponse getMe() {
        return meService.getMe(AuthContext.requireUserId());
    }

    @GetMapping("/articles")
    public MyArticlesResponse listArticles(
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return meService.listArticles(AuthContext.requireUserId(), status, page, size);
    }

    @GetMapping("/topics")
    public TopicListResponse myTopics(@RequestParam(defaultValue = "created") String role) {
        return topicService.listMyTopics(AuthContext.requireUserId(), role);
    }

    @GetMapping("/account")
    public AccountSecurityResponse accountSecurity() {
        return meAccountService.getAccountSecurity(AuthContext.requireUserId());
    }

    @PostMapping("/phone/bind")
    public void bindPhone(@RequestBody BindPhoneRequest request) {
        meAccountService.bindPhone(AuthContext.requireUserId(), request);
    }

    @PostMapping("/password")
    public void changePassword(@RequestBody ChangePasswordRequest request) {
        meAccountService.changePassword(AuthContext.requireUserId(), request);
    }

    @PostMapping("/account-name")
    public void changeAccountName(@RequestBody ChangeAccountNameRequest request) {
        meAccountService.changeAccountName(AuthContext.requireUserId(), request);
    }

    @PostMapping("/wechat-profile")
    public UserSummary updateWechatProfile(@RequestBody WechatProfileRequest request) {
        return meAccountService.updateWechatProfile(AuthContext.requireUserId(), request);
    }

    @DeleteMapping
    public void deactivate() {
        meAccountService.deactivateAccount(AuthContext.requireUserId());
    }
}
