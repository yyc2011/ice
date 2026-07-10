package com.ice.controller;

import com.ice.dto.auth.AdminLoginRequest;
import com.ice.dto.auth.DevLoginRequest;
import com.ice.dto.auth.DevLoginResponse;
import com.ice.dto.auth.LoginResponse;
import com.ice.dto.auth.WechatLoginRequest;
import com.ice.dto.auth.PasswordLoginRequest;
import com.ice.dto.auth.PhoneLoginRequest;
import com.ice.dto.auth.SmsCodeRequest;
import com.ice.dto.auth.SmsCodeResponse;
import com.ice.service.AuthService;
import com.ice.service.MeAccountService;
import com.ice.service.SmsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final SmsService smsService;
    private final MeAccountService meAccountService;

    public AuthController(
            AuthService authService,
            SmsService smsService,
            MeAccountService meAccountService
    ) {
        this.authService = authService;
        this.smsService = smsService;
        this.meAccountService = meAccountService;
    }

    @PostMapping("/dev/login")
    public DevLoginResponse devLogin(@RequestBody(required = false) DevLoginRequest request) {
        String openid = request == null ? null : request.openid();
        return authService.devLogin(openid);
    }

    @PostMapping("/wechat/login")
    public LoginResponse wechatLogin(@RequestBody WechatLoginRequest request) {
        return authService.wechatLogin(request.code());
    }

    @PostMapping("/admin/login")
    public LoginResponse adminLogin(@RequestBody AdminLoginRequest request) {
        return authService.adminLogin(request);
    }

    @PostMapping("/sms/code")
    public SmsCodeResponse sendSmsCode(@RequestBody SmsCodeRequest request) {
        String purpose = request.purpose() == null ? "login" : request.purpose();
        smsService.sendCode(request.phone(), purpose);
        return new SmsCodeResponse("验证码已发送", smsService.getCodeExpireSeconds());
    }

    @PostMapping("/phone/login")
    public LoginResponse phoneLogin(@RequestBody PhoneLoginRequest request) {
        return meAccountService.phoneLogin(request.phone(), request.code());
    }

    @PostMapping("/password/login")
    public LoginResponse passwordLogin(@RequestBody PasswordLoginRequest request) {
        return meAccountService.passwordLogin(request.account_name(), request.password());
    }
}
