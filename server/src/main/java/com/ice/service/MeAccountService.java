package com.ice.service;

import com.ice.auth.JwtService;
import com.ice.domain.UserLevel;
import com.ice.domain.UserRole;
import com.ice.dto.auth.LoginResponse;
import com.ice.dto.auth.UserSummary;
import com.ice.dto.me.AccountSecurityResponse;
import com.ice.dto.me.BindPhoneRequest;
import com.ice.dto.me.ChangeAccountNameRequest;
import com.ice.dto.me.ChangePasswordRequest;
import com.ice.dto.me.WechatProfileRequest;
import com.ice.exception.ApiException;
import com.ice.generated.jooq.tables.records.UserRecord;
import java.time.LocalDateTime;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ice.generated.jooq.tables.User.USER;

@Service
public class MeAccountService {

    private static final Field<String> PASSWORD_HASH = DSL.field("password_hash", String.class);
    private static final Field<String> PHONE = DSL.field("phone", String.class);
    private static final Field<Byte> PHONE_VERIFIED = DSL.field("phone_verified", Byte.class);

    private final DSLContext dsl;
    private final AuthService authService;
    private final SmsService smsService;
    private final JwtService jwtService;
    private final UserIdentityService userIdentityService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public MeAccountService(
            DSLContext dsl,
            AuthService authService,
            SmsService smsService,
            JwtService jwtService,
            UserIdentityService userIdentityService
    ) {
        this.dsl = dsl;
        this.authService = authService;
        this.smsService = smsService;
        this.jwtService = jwtService;
        this.userIdentityService = userIdentityService;
    }

    public AccountSecurityResponse getAccountSecurity(long userId) {
        UserRecord user = requireUser(userId);
        String phone = user.get(PHONE);
        String masked = maskPhone(phone);
        boolean phoneVerified = user.get(PHONE_VERIFIED) != null && user.get(PHONE_VERIFIED) == 1;
        boolean passwordSet = user.get(PASSWORD_HASH) != null && !user.get(PASSWORD_HASH).isBlank();
        boolean wechatBound = user.getOpenid() != null && !user.getOpenid().isBlank();
        String accountName = user.getAccountName();
        boolean accountNameSet = userIdentityService.isAccountNameSet(accountName);
        LocalDateTime changedAt = user.getAccountNameChangedAt();
        boolean canChange = accountNameSet && userIdentityService.canChangeAccountName(changedAt);
        LocalDateTime nextChangeAt = userIdentityService.nextAccountNameChangeAt(changedAt);
        return new AccountSecurityResponse(
                masked,
                phoneVerified,
                passwordSet,
                wechatBound,
                accountName,
                accountNameSet,
                canChange,
                nextChangeAt == null ? null : nextChangeAt.toLocalDate().toString()
        );
    }

    @Transactional
    public void bindPhone(long userId, BindPhoneRequest request) {
        smsService.verifyCode(request.phone(), "bind", request.code());
        String phone = request.phone().trim();
        UserRecord existing = dsl.selectFrom(USER).where(PHONE.eq(phone)).fetchOne();
        if (existing != null && !existing.getId().equals(userId)) {
            throw new ApiException("PHONE_ALREADY_BOUND", "该手机号已被其他账号绑定", HttpStatus.CONFLICT);
        }
        dsl.update(USER)
                .set(PHONE, phone)
                .set(PHONE_VERIFIED, (byte) 1)
                .set(USER.UPDATED_AT, LocalDateTime.now())
                .where(USER.ID.eq(userId))
                .execute();
    }

    @Transactional
    public void changePassword(long userId, ChangePasswordRequest request) {
        if (request.new_password() == null || request.new_password().length() < 6) {
            throw new ApiException("VALIDATION_ERROR", "新密码至少 6 位", HttpStatus.BAD_REQUEST);
        }
        if (request.confirm_password() == null
                || !request.new_password().equals(request.confirm_password())) {
            throw new ApiException("PASSWORD_MISMATCH", "两次输入的密码不一致", HttpStatus.BAD_REQUEST);
        }
        UserRecord user = requireUser(userId);
        String currentHash = user.get(PASSWORD_HASH);
        String currentAccountName = user.getAccountName();
        boolean accountNameSet = userIdentityService.isAccountNameSet(currentAccountName);
        LocalDateTime now = LocalDateTime.now();

        if (!accountNameSet) {
            if (request.account_name() == null || request.account_name().isBlank()) {
                throw new ApiException("ACCOUNT_NAME_REQUIRED", "首次设置密码须同时设置账号", HttpStatus.BAD_REQUEST);
            }
            userIdentityService.ensureAccountNameAvailable(request.account_name(), userId);
            dsl.update(USER)
                    .set(USER.ACCOUNT_NAME, userIdentityService.normalizeAccountName(request.account_name()))
                    .set(USER.ACCOUNT_NAME_CHANGED_AT, now)
                    .set(PASSWORD_HASH, passwordEncoder.encode(request.new_password()))
                    .set(USER.UPDATED_AT, now)
                    .where(USER.ID.eq(userId))
                    .execute();
            return;
        }

        if (currentHash != null && !currentHash.isBlank()) {
            if (request.old_password() == null
                    || !passwordEncoder.matches(request.old_password(), currentHash)) {
                throw new ApiException("PASSWORD_INCORRECT", "原密码错误", HttpStatus.BAD_REQUEST);
            }
        }
        dsl.update(USER)
                .set(PASSWORD_HASH, passwordEncoder.encode(request.new_password()))
                .set(USER.UPDATED_AT, now)
                .where(USER.ID.eq(userId))
                .execute();
    }

    @Transactional
    public void changeAccountName(long userId, ChangeAccountNameRequest request) {
        UserRecord user = requireUser(userId);
        String currentHash = user.get(PASSWORD_HASH);
        if (currentHash == null || currentHash.isBlank()) {
            throw new ApiException("PASSWORD_LOGIN_NOT_SET", "请先设置登录密码", HttpStatus.BAD_REQUEST);
        }
        if (!userIdentityService.isAccountNameSet(user.getAccountName())) {
            throw new ApiException("ACCOUNT_NAME_NOT_SET", "请先设置账号", HttpStatus.BAD_REQUEST);
        }
        if (request.password() == null || !passwordEncoder.matches(request.password(), currentHash)) {
            throw new ApiException("PASSWORD_INCORRECT", "密码错误", HttpStatus.BAD_REQUEST);
        }
        userIdentityService.assertCanChangeAccountName(user.getAccountNameChangedAt());
        userIdentityService.ensureAccountNameAvailable(request.new_account_name(), userId);
        LocalDateTime now = LocalDateTime.now();
        dsl.update(USER)
                .set(USER.ACCOUNT_NAME, userIdentityService.normalizeAccountName(request.new_account_name()))
                .set(USER.ACCOUNT_NAME_CHANGED_AT, now)
                .set(USER.UPDATED_AT, now)
                .where(USER.ID.eq(userId))
                .execute();
    }

    @Transactional
    public UserSummary updateWechatProfile(long userId, WechatProfileRequest request) {
        if (request.nickname() == null || request.nickname().isBlank()) {
            throw new ApiException("VALIDATION_ERROR", "昵称不能为空", HttpStatus.BAD_REQUEST);
        }
        dsl.update(USER)
                .set(USER.NICKNAME, request.nickname().trim())
                .set(USER.AVATAR_URL, request.avatar_url())
                .set(USER.UPDATED_AT, LocalDateTime.now())
                .where(USER.ID.eq(userId))
                .execute();
        UserRecord user = requireUser(userId);
        return authService.toSummary(user);
    }

    @Transactional
    public void deactivateAccount(long userId) {
        requireUser(userId);
        dsl.update(USER)
                .set(USER.STATUS, (byte) 1)
                .set(USER.OPENID, (String) null)
                .set(PHONE, (String) null)
                .set(PASSWORD_HASH, (String) null)
                .set(USER.ACCOUNT_NAME, (String) null)
                .set(USER.ACCOUNT_NAME_CHANGED_AT, (LocalDateTime) null)
                .set(PHONE_VERIFIED, (byte) 0)
                .set(USER.NICKNAME, "已注销用户")
                .set(USER.AVATAR_URL, (String) null)
                .set(USER.UPDATED_AT, LocalDateTime.now())
                .where(USER.ID.eq(userId))
                .execute();
    }

    @Transactional
    public LoginResponse phoneLogin(String phone, String code) {
        smsService.verifyCode(phone, "login", code);
        String normalized = phone.trim();
        UserRecord user = dsl.selectFrom(USER).where(PHONE.eq(normalized)).fetchOne();
        if (user == null) {
            user = authService.createPhoneUser(normalized);
        }
        if (user == null) {
            throw new ApiException("INTERNAL_ERROR", "创建用户失败", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        authService.ensureActivePublic(user);
        return authService.issueLoginResponsePublic(user);
    }

    public LoginResponse passwordLogin(String accountName, String password) {
        if (accountName == null || password == null) {
            throw new ApiException("VALIDATION_ERROR", "账号和密码不能为空", HttpStatus.BAD_REQUEST);
        }
        String normalized = userIdentityService.normalizeAccountName(accountName);
        UserRecord user = dsl.selectFrom(USER).where(USER.ACCOUNT_NAME.eq(normalized)).fetchOne();
        if (user == null) {
            throw new ApiException("ACCOUNT_NAME_NOT_SET", "账号不存在或未设置", HttpStatus.BAD_REQUEST);
        }
        String hash = user.get(PASSWORD_HASH);
        if (hash == null || hash.isBlank()) {
            throw new ApiException("PASSWORD_LOGIN_NOT_SET", "尚未设置登录密码", HttpStatus.BAD_REQUEST);
        }
        if (!passwordEncoder.matches(password, hash)) {
            throw new ApiException("PASSWORD_INCORRECT", "密码错误", HttpStatus.BAD_REQUEST);
        }
        authService.ensureActivePublic(user);
        return authService.issueLoginResponsePublic(user);
    }

    private UserRecord requireUser(long userId) {
        UserRecord user = dsl.selectFrom(USER).where(USER.ID.eq(userId)).fetchOne();
        if (user == null) {
            throw new ApiException("UNAUTHORIZED", "用户不存在", HttpStatus.UNAUTHORIZED);
        }
        return user;
    }

    private String maskPhone(String phone) {
        if (phone == null || phone.length() < 7) {
            return null;
        }
        return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
    }
}
