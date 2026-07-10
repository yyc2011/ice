package com.ice.service;

import com.ice.dto.config.ClientConfigResponse;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;

@Service
public class ConfigService {

    private static final Field<String> CONFIG_KEY = DSL.field("config_key", String.class);
    private static final Field<String> CONFIG_VALUE = DSL.field("config_value", String.class);
    private static final org.jooq.Table<?> FEATURE_CONFIG = DSL.table("feature_config");

    private final DSLContext dsl;

    public ConfigService(DSLContext dsl) {
        this.dsl = dsl;
    }

    public ClientConfigResponse getClientConfig() {
        return new ClientConfigResponse(
                readBool("recharge_enabled", false),
                readBool("recharge_custom_amount_enabled", false)
        );
    }

    private boolean readBool(String key, boolean defaultValue) {
        String value = dsl.select(CONFIG_VALUE)
                .from(FEATURE_CONFIG)
                .where(CONFIG_KEY.eq(key))
                .fetchOne(CONFIG_VALUE);
        if (value == null) {
            return defaultValue;
        }
        return "true".equalsIgnoreCase(value) || "1".equals(value);
    }
}
