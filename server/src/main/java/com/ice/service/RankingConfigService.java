package com.ice.service;

import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import static com.ice.generated.jooq.tables.RankingConfig.RANKING_CONFIG;

@Service
public class RankingConfigService {

    private final DSLContext dsl;

    public RankingConfigService(DSLContext dsl) {
        this.dsl = dsl;
    }

    public double getDouble(String key, double defaultValue) {
        String value = dsl.select(RANKING_CONFIG.CONFIG_VALUE)
                .from(RANKING_CONFIG)
                .where(RANKING_CONFIG.CONFIG_KEY.eq(key))
                .fetchOne(RANKING_CONFIG.CONFIG_VALUE);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }

    public int getInt(String key, int defaultValue) {
        String value = dsl.select(RANKING_CONFIG.CONFIG_VALUE)
                .from(RANKING_CONFIG)
                .where(RANKING_CONFIG.CONFIG_KEY.eq(key))
                .fetchOne(RANKING_CONFIG.CONFIG_VALUE);
        if (value == null) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException ex) {
            return defaultValue;
        }
    }
}
