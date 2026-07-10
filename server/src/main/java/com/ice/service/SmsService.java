package com.ice.service;

import com.ice.config.SmsProperties;
import com.ice.exception.ApiException;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.jooq.DSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ice.generated.jooq.tables.SmsCode.SMS_CODE;

@Service
public class SmsService {

    private static final Logger log = LoggerFactory.getLogger(SmsService.class);
    private static final SecureRandom RANDOM = new SecureRandom();

    private final DSLContext dsl;
    private final SmsProperties smsProperties;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public SmsService(DSLContext dsl, SmsProperties smsProperties) {
        this.dsl = dsl;
        this.smsProperties = smsProperties;
    }

    @Transactional
    public void sendCode(String phone, String purpose) {
        String normalized = normalizePhone(phone);
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cooldownSince = now.minusSeconds(smsProperties.getSendCooldownSeconds());
        int recent = dsl.fetchCount(
                dsl.selectFrom(SMS_CODE)
                        .where(SMS_CODE.PHONE.eq(normalized))
                        .and(SMS_CODE.PURPOSE.eq(purpose))
                        .and(SMS_CODE.CREATED_AT.ge(cooldownSince))
        );
        if (recent > 0) {
            throw new ApiException("SMS_SEND_TOO_FREQUENT", "发送过于频繁，请稍后再试", HttpStatus.TOO_MANY_REQUESTS);
        }
        LocalDateTime dayStart = LocalDate.now().atStartOfDay();
        int dailyCount = dsl.fetchCount(
                dsl.selectFrom(SMS_CODE)
                        .where(SMS_CODE.PHONE.eq(normalized))
                        .and(SMS_CODE.CREATED_AT.ge(dayStart))
        );
        if (dailyCount >= smsProperties.getDailyLimit()) {
            throw new ApiException("SMS_SEND_TOO_FREQUENT", "今日验证码发送次数已达上限", HttpStatus.TOO_MANY_REQUESTS);
        }

        String code = smsProperties.isMockEnabled()
                ? smsProperties.getMockCode()
                : String.format("%06d", RANDOM.nextInt(1_000_000));
        if (smsProperties.isMockEnabled()) {
            log.info("[SMS MOCK] phone={} purpose={} code={}", normalized, purpose, code);
        } else {
            // TODO: integrate Tencent Cloud SMS SDK
            log.warn("SMS production mode not configured, falling back to mock");
        }

        dsl.insertInto(SMS_CODE)
                .set(SMS_CODE.PHONE, normalized)
                .set(SMS_CODE.CODE_HASH, passwordEncoder.encode(code))
                .set(SMS_CODE.PURPOSE, purpose)
                .set(SMS_CODE.EXPIRE_AT, now.plusSeconds(smsProperties.getCodeExpireSeconds()))
                .set(SMS_CODE.CREATED_AT, now)
                .execute();
    }

    @Transactional
    public void verifyCode(String phone, String purpose, String code) {
        String normalized = normalizePhone(phone);
        if (code == null || code.isBlank()) {
            throw new ApiException("SMS_CODE_INVALID", "验证码错误", HttpStatus.BAD_REQUEST);
        }
        var record = dsl.selectFrom(SMS_CODE)
                .where(SMS_CODE.PHONE.eq(normalized))
                .and(SMS_CODE.PURPOSE.eq(purpose))
                .and(SMS_CODE.CONSUMED_AT.isNull())
                .and(SMS_CODE.EXPIRE_AT.gt(LocalDateTime.now()))
                .orderBy(SMS_CODE.ID.desc())
                .limit(1)
                .fetchOne();
        if (record == null || !passwordEncoder.matches(code.trim(), record.getCodeHash())) {
            throw new ApiException("SMS_CODE_INVALID", "验证码错误或已过期", HttpStatus.BAD_REQUEST);
        }
        dsl.update(SMS_CODE)
                .set(SMS_CODE.CONSUMED_AT, LocalDateTime.now())
                .where(SMS_CODE.ID.eq(record.getId()))
                .execute();
    }

    public int getCodeExpireSeconds() {
        return smsProperties.getCodeExpireSeconds();
    }

    private String normalizePhone(String phone) {
        if (phone == null || !phone.matches("^1\\d{10}$")) {
            throw new ApiException("VALIDATION_ERROR", "手机号格式不正确", HttpStatus.BAD_REQUEST);
        }
        return phone.trim();
    }
}
