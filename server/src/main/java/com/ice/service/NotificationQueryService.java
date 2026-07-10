package com.ice.service;

import com.ice.dto.notification.NotificationItemDto;
import com.ice.dto.notification.NotificationListResponse;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import static com.ice.generated.jooq.tables.Notification.NOTIFICATION;

@Service
public class NotificationQueryService {

    private final DSLContext dsl;

    public NotificationQueryService(DSLContext dsl) {
        this.dsl = dsl;
    }

    public NotificationListResponse listForUser(long userId, int page, int size) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.min(Math.max(size, 1), 50);
        int offset = (safePage - 1) * safeSize;

        long total = dsl.fetchCount(
                dsl.selectFrom(NOTIFICATION).where(NOTIFICATION.USER_ID.eq(userId))
        );

        List<NotificationItemDto> items = dsl.selectFrom(NOTIFICATION)
                .where(NOTIFICATION.USER_ID.eq(userId))
                .orderBy(NOTIFICATION.CREATED_AT.desc())
                .limit(safeSize)
                .offset(offset)
                .fetch(record -> new NotificationItemDto(
                        record.getId(),
                        record.getType(),
                        record.getTitle(),
                        record.getContent(),
                        record.getRefType(),
                        record.getRefId(),
                        record.getIsRead() != null && record.getIsRead() == 1,
                        toInstant(record.getCreatedAt())
                ));

        return new NotificationListResponse(items, total, safePage, safeSize);
    }

    public void markRead(long userId, List<Long> ids) {
        var update = dsl.update(NOTIFICATION)
                .set(NOTIFICATION.IS_READ, (byte) 1)
                .where(NOTIFICATION.USER_ID.eq(userId));
        if (ids != null && !ids.isEmpty()) {
            update = update.and(NOTIFICATION.ID.in(ids));
        }
        update.execute();
    }

    private Instant toInstant(LocalDateTime time) {
        return time == null ? null : time.atOffset(ZoneOffset.UTC).toInstant();
    }
}
