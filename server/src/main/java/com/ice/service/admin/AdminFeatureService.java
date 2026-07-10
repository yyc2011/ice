package com.ice.service.admin;

import com.ice.dto.admin.FeatureConfigDto;
import com.ice.dto.admin.FeatureUpdateRequest;
import java.util.List;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminFeatureService {

    private static final Field<String> CONFIG_KEY = DSL.field("config_key", String.class);
    private static final Field<String> CONFIG_VALUE = DSL.field("config_value", String.class);
    private static final org.jooq.Table<?> FEATURE_CONFIG = DSL.table("feature_config");

    private final DSLContext dsl;

    public AdminFeatureService(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<FeatureConfigDto> list() {
        return dsl.select(CONFIG_KEY, CONFIG_VALUE)
                .from(FEATURE_CONFIG)
                .fetch(record -> new FeatureConfigDto(
                        record.get(CONFIG_KEY),
                        record.get(CONFIG_VALUE)
                ));
    }

    @Transactional
    public void update(FeatureUpdateRequest request) {
        if (request.items() == null) {
            return;
        }
        for (var item : request.items()) {
            dsl.update(FEATURE_CONFIG)
                    .set(CONFIG_VALUE, item.value())
                    .where(CONFIG_KEY.eq(item.key()))
                    .execute();
        }
    }
}
