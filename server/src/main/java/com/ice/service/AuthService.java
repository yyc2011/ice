package com.ice.service;

import com.ice.auth.JwtService;
import com.ice.client.WechatAuthClient;
import com.ice.config.AuthProperties;
import com.ice.domain.UserLevel;
import com.ice.domain.UserRole;
import com.ice.dto.auth.AdminLoginRequest;
import com.ice.dto.auth.DevLoginResponse;
import com.ice.dto.auth.LoginResponse;
import com.ice.dto.auth.UserSummary;
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
public class AuthService {

    private static final Field<String> LOGIN_NAME = DSL.field("login_name", String.class);
    private static final Field<String> PASSWORD_HASH = DSL.field("password_hash", String.class);

    private final DSLContext dsl;
    private final JwtService jwtService;
    private final WechatAuthClient wechatAuthClient;
    private final AuthProperties authProperties;
    private final UserIdentityService userIdentityService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthService(
            DSLContext dsl,
            JwtService jwtService,
            WechatAuthClient wechatAuthClient,
            AuthProperties authProperties,
            UserIdentityService userIdentityService
    ) {
        this.dsl = dsl;
        this.jwtService = jwtService;
        this.wechatAuthClient = wechatAuthClient;
        this.authProperties = authProperties;
        this.userIdentityService = userIdentityService;
    }

    public DevLoginResponse devLogin(String openid) {
        if (!authProperties.isDevLoginEnabled()) {
            throw new ApiException("FORBIDDEN", "开发登录已关闭", HttpStatus.FORBIDDEN);
        }
        String resolvedOpenid = (openid == null || openid.isBlank()) ? "dev-test" : openid.trim();
        UserRecord user = findOrCreateByOpenid(resolvedOpenid);
        ensureActive(user);
        return new DevLoginResponse(
                "dev-" + user.getId(),
                jwtService.getExpireSeconds(),
                toSummary(user)
        );
    }

    @Transactional
    public LoginResponse wechatLogin(String code) {
        String openid = wechatAuthClient.codeToOpenid(code);
        UserRecord user = dsl.selectFrom(USER).where(USER.OPENID.eq(openid)).fetchOne();
        if (user == null) {
            user = createWechatUser(openid);
        }
        if (user == null) {
            throw new ApiException("INTERNAL_ERROR", "创建用户失败", HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ensureActive(user);
        return issueLoginResponse(user);
    }

    public LoginResponse adminLogin(AdminLoginRequest request) {
        if (request == null || request.login_name() == null || request.login_name().isBlank()) {
            throw new ApiException("ADMIN_LOGIN_FAILED", "用户名或密码错误", HttpStatus.BAD_REQUEST);
        }
        UserRecord user = dsl.selectFrom(USER)
                .where(LOGIN_NAME.eq(request.login_name().trim()))
                .fetchOne();
        if (user == null) {
            throw new ApiException("ADMIN_LOGIN_FAILED", "用户名或密码错误", HttpStatus.BAD_REQUEST);
        }
        byte roleCode = user.getRole() == null ? UserRole.USER.code() : user.getRole();
        if (!UserRole.canAccessAdmin(roleCode)) {
            throw new ApiException("FORBIDDEN", "非管理员账号", HttpStatus.FORBIDDEN);
        }
        String hash = user.get(PASSWORD_HASH);
        if (hash == null || !passwordEncoder.matches(
                request.password() == null ? "" : request.password(),
                hash
        )) {
            throw new ApiException("ADMIN_LOGIN_FAILED", "用户名或密码错误", HttpStatus.BAD_REQUEST);
        }
        ensureActive(user);
        return issueLoginResponse(user);
    }

    public UserRole loadRole(long userId) {
        Byte role = dsl.select(USER.ROLE).from(USER).where(USER.ID.eq(userId)).fetchOne(USER.ROLE);
        if (role == null) {
            return UserRole.USER;
        }
        return UserRole.fromCode(role);
    }

    public PasswordEncoder passwordEncoder() {
        return passwordEncoder;
    }

    public void ensureActivePublic(UserRecord user) {
        ensureActive(user);
    }

    public LoginResponse issueLoginResponsePublic(UserRecord user) {
        return issueLoginResponse(user);
    }

    @Transactional
    public UserRecord createWechatUser(String openid) {
        LocalDateTime now = LocalDateTime.now();
        Long userId = dsl.insertInto(USER)
                .set(USER.INTERNAL_UID, userIdentityService.generateUniqueInternalUid())
                .set(USER.OPENID, openid)
                .set(USER.NICKNAME, "微信用户")
                .set(USER.LEVEL, (short) UserLevel.READER.code())
                .set(USER.ROLE, UserRole.USER.code())
                .set(USER.CREATED_AT, now)
                .set(USER.UPDATED_AT, now)
                .returningResult(USER.ID)
                .fetchOne(USER.ID);
        return dsl.selectFrom(USER).where(USER.ID.eq(userId)).fetchOne();
    }

    @Transactional
    public UserRecord createPhoneUser(String phone) {
        LocalDateTime now = LocalDateTime.now();
        Long userId = dsl.insertInto(USER)
                .set(USER.INTERNAL_UID, userIdentityService.generateUniqueInternalUid())
                .set(USER.NICKNAME, "手机用户" + (1000 + (int) (Math.random() * 9000)))
                .set(USER.PHONE, phone)
                .set(USER.PHONE_VERIFIED, (byte) 1)
                .set(USER.LEVEL, (short) UserLevel.READER.code())
                .set(USER.ROLE, UserRole.USER.code())
                .set(USER.CREATED_AT, now)
                .set(USER.UPDATED_AT, now)
                .returningResult(USER.ID)
                .fetchOne(USER.ID);
        return dsl.selectFrom(USER).where(USER.ID.eq(userId)).fetchOne();
    }

    private UserRecord findOrCreateByOpenid(String openid) {
        UserRecord user = dsl.selectFrom(USER).where(USER.OPENID.eq(openid)).fetchOne();
        if (user != null) {
            return user;
        }
        return createWechatUser(openid);
    }

    private void ensureActive(UserRecord user) {
        if (user.getStatus() != null && user.getStatus() == 1) {
            throw new ApiException("USER_BANNED", "账号已封禁", HttpStatus.FORBIDDEN);
        }
    }

    private LoginResponse issueLoginResponse(UserRecord user) {
        byte roleCode = user.getRole() == null ? UserRole.USER.code() : user.getRole();
        String token = jwtService.issueToken(user.getId(), roleCode);
        return new LoginResponse(token, jwtService.getExpireSeconds(), toSummary(user));
    }

    public UserSummary toSummary(UserRecord user) {
        byte levelCode = user.getLevel() == null ? UserLevel.READER.code() : user.getLevel().byteValue();
        byte roleCode = user.getRole() == null ? UserRole.USER.code() : user.getRole();
        UserLevel level = UserLevel.fromCode(levelCode);
        UserRole role = UserRole.fromCode(roleCode);
        return new UserSummary(
                user.getId(),
                user.getNickname(),
                user.getAvatarUrl(),
                level.apiValue(),
                level.displayName(),
                user.getBookCoinBalance() == null ? 0 : user.getBookCoinBalance(),
                role.apiValue()
        );
    }
}
