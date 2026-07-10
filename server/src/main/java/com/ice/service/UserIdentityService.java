package com.ice.service;

import com.ice.exception.ApiException;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.regex.Pattern;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import static com.ice.generated.jooq.tables.User.USER;

@Service
public class UserIdentityService {

    private static final Pattern ACCOUNT_NAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_]{1,15}$");
    private static final Set<String> RESERVED_ACCOUNT_NAMES = Set.of("admin", "root", "system");
    private static final long ACCOUNT_NAME_CHANGE_COOLDOWN_DAYS = 90L;
    private static final long MIN_INTERNAL_UID = 100_000_000_000_000L;
    private static final long MAX_INTERNAL_UID = 999_999_999_999_999L;

    private static final Field<String> INTERNAL_UID = USER.INTERNAL_UID;
    private static final Field<String> ACCOUNT_NAME = USER.ACCOUNT_NAME;
    private static final Field<LocalDateTime> ACCOUNT_NAME_CHANGED_AT =
            DSL.field("account_name_changed_at", LocalDateTime.class);

    private final DSLContext dsl;
    private final SecureRandom secureRandom = new SecureRandom();

    public UserIdentityService(DSLContext dsl) {
        this.dsl = dsl;
    }

    public String generateUniqueInternalUid() {
        for (int attempt = 0; attempt < 32; attempt++) {
            String candidate = String.valueOf(
                    MIN_INTERNAL_UID + (long) (secureRandom.nextDouble() * (MAX_INTERNAL_UID - MIN_INTERNAL_UID))
            );
            if (candidate.length() != 15) {
                continue;
            }
            Integer exists = dsl.selectCount()
                    .from(USER)
                    .where(INTERNAL_UID.eq(candidate))
                    .fetchOne(0, int.class);
            if (exists != null && exists == 0) {
                return candidate;
            }
        }
        throw new ApiException("INTERNAL_ERROR", "生成内部标识失败", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public String normalizeAccountName(String accountName) {
        if (accountName == null) {
            return null;
        }
        return accountName.trim();
    }

    public void validateAccountNameFormat(String accountName) {
        String normalized = normalizeAccountName(accountName);
        if (normalized == null || normalized.isBlank()) {
            throw new ApiException("ACCOUNT_NAME_INVALID", "账号格式不合法", HttpStatus.BAD_REQUEST);
        }
        if (!ACCOUNT_NAME_PATTERN.matcher(normalized).matches()) {
            throw new ApiException("ACCOUNT_NAME_INVALID", "账号仅可包含字母、数字和下划线，最长 15 位", HttpStatus.BAD_REQUEST);
        }
        if (RESERVED_ACCOUNT_NAMES.contains(normalized.toLowerCase())) {
            throw new ApiException("ACCOUNT_NAME_INVALID", "该账号不可使用", HttpStatus.BAD_REQUEST);
        }
    }

    public void ensureAccountNameAvailable(String accountName, Long excludeUserId) {
        validateAccountNameFormat(accountName);
        String normalized = normalizeAccountName(accountName);
        var condition = ACCOUNT_NAME.eq(normalized);
        if (excludeUserId != null) {
            condition = condition.and(USER.ID.ne(excludeUserId));
        }
        Integer exists = dsl.selectCount().from(USER).where(condition).fetchOne(0, int.class);
        if (exists != null && exists > 0) {
            throw new ApiException("ACCOUNT_NAME_TAKEN", "账号已被占用", HttpStatus.CONFLICT);
        }
    }

    public boolean isAccountNameSet(String accountName) {
        return accountName != null && !accountName.isBlank();
    }

    public boolean canChangeAccountName(LocalDateTime changedAt) {
        if (changedAt == null) {
            return true;
        }
        return ChronoUnit.DAYS.between(changedAt, LocalDateTime.now()) >= ACCOUNT_NAME_CHANGE_COOLDOWN_DAYS;
    }

    public LocalDateTime nextAccountNameChangeAt(LocalDateTime changedAt) {
        if (changedAt == null) {
            return null;
        }
        return changedAt.plusDays(ACCOUNT_NAME_CHANGE_COOLDOWN_DAYS);
    }

    public void assertCanChangeAccountName(LocalDateTime changedAt) {
        if (!canChangeAccountName(changedAt)) {
            LocalDateTime nextAt = nextAccountNameChangeAt(changedAt);
            throw new ApiException(
                    "ACCOUNT_NAME_CHANGE_LIMIT",
                    "90 天内仅可修改 1 次，下次可修改时间：" + nextAt.toLocalDate(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
