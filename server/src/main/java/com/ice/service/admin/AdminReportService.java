package com.ice.service.admin;

import com.ice.dto.admin.ReportDto;
import com.ice.dto.admin.ReportResolveRequest;
import com.ice.exception.ApiException;
import java.time.LocalDateTime;
import java.util.List;
import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.impl.DSL;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminReportService {

    private static final Field<Long> ID = DSL.field("id", Long.class);
    private static final Field<Long> REPORTER_ID = DSL.field("reporter_id", Long.class);
    private static final Field<Byte> TARGET_TYPE = DSL.field("target_type", Byte.class);
    private static final Field<Long> TARGET_ID = DSL.field("target_id", Long.class);
    private static final Field<String> REASON = DSL.field("reason", String.class);
    private static final Field<String> REASON_DETAIL = DSL.field("reason_detail", String.class);
    private static final Field<Byte> STATUS = DSL.field("status", Byte.class);
    private static final Field<LocalDateTime> CREATED_AT = DSL.field("created_at", LocalDateTime.class);
    private static final org.jooq.Table<?> REPORT = DSL.table("report");

    private final DSLContext dsl;

    public AdminReportService(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<ReportDto> list() {
        return dsl.select(ID, REPORTER_ID, TARGET_TYPE, TARGET_ID, REASON, REASON_DETAIL, STATUS, CREATED_AT)
                .from(REPORT)
                .where(STATUS.eq((byte) 0))
                .orderBy(CREATED_AT.desc())
                .fetch(record -> new ReportDto(
                        record.get(ID),
                        record.get(REPORTER_ID),
                        record.get(TARGET_TYPE) == null ? 0 : record.get(TARGET_TYPE).intValue(),
                        record.get(TARGET_ID),
                        record.get(REASON),
                        record.get(REASON_DETAIL),
                        record.get(STATUS) == null ? 0 : record.get(STATUS).intValue(),
                        record.get(CREATED_AT).toString()
                ));
    }

    @Transactional
    public void resolve(long id, ReportResolveRequest request) {
        boolean accepted = Boolean.TRUE.equals(request.accepted());
        int updated = dsl.update(REPORT)
                .set(STATUS, (byte) (accepted ? 1 : 2))
                .where(ID.eq(id).and(STATUS.eq((byte) 0)))
                .execute();
        if (updated == 0) {
            throw new ApiException("NOT_FOUND", "举报不存在或已处理", HttpStatus.NOT_FOUND);
        }
    }
}
