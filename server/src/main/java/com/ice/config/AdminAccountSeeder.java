package com.ice.config;

import com.ice.domain.UserRole;
import com.ice.service.AuthService;
import com.ice.service.UserIdentityService;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import static com.ice.generated.jooq.tables.User.USER;

@Component
public class AdminAccountSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminAccountSeeder.class);
    private static final Field<String> LOGIN_NAME = DSL.field("login_name", String.class);
    private static final Field<String> PASSWORD_HASH = DSL.field("password_hash", String.class);

    private final DSLContext dsl;
    private final AuthService authService;
    private final AuthProperties authProperties;
    private final UserIdentityService userIdentityService;

    public AdminAccountSeeder(
            DSLContext dsl,
            AuthService authService,
            AuthProperties authProperties,
            UserIdentityService userIdentityService
    ) {
        this.dsl = dsl;
        this.authService = authService;
        this.authProperties = authProperties;
        this.userIdentityService = userIdentityService;
    }

    @Override
    public void run(ApplicationArguments args) {
        Integer count = dsl.selectCount()
                .from(USER)
                .where(LOGIN_NAME.eq("admin"))
                .fetchOne(0, int.class);
        if (count != null && count > 0) {
            return;
        }
        String password = authProperties.getAdminSeedPassword();
        String hash = authService.passwordEncoder().encode(password);
        dsl.insertInto(USER)
                .set(USER.INTERNAL_UID, userIdentityService.generateUniqueInternalUid())
                .set(USER.OPENID, (String) null)
                .set(LOGIN_NAME, "admin")
                .set(PASSWORD_HASH, hash)
                .set(USER.NICKNAME, "系统管理员")
                .set(USER.ROLE, UserRole.SUPER_ADMIN.code())
                .set(USER.CREATED_AT, java.time.LocalDateTime.now())
                .set(USER.UPDATED_AT, java.time.LocalDateTime.now())
                .execute();
        log.info("已创建种子超级管理员 login_name=admin（密码来自 ice.auth.admin-seed-password）");
    }
}
