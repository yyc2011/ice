package com.ice.service.admin;

import com.ice.dto.admin.AnnouncementDto;
import com.ice.dto.admin.CreateAnnouncementRequest;
import com.ice.exception.ApiException;
import java.time.LocalDateTime;
import java.util.List;
import org.jooq.DSLContext;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ice.generated.jooq.tables.Announcement.ANNOUNCEMENT;
import static com.ice.generated.jooq.tables.Notification.NOTIFICATION;

@Service
public class AdminAnnouncementService {

    private final DSLContext dsl;

    public AdminAnnouncementService(DSLContext dsl) {
        this.dsl = dsl;
    }

    public List<AnnouncementDto> list() {
        return dsl.selectFrom(ANNOUNCEMENT)
                .orderBy(ANNOUNCEMENT.CREATED_AT.desc())
                .fetch(record -> new AnnouncementDto(
                        record.getId(),
                        record.getTitle(),
                        record.getContent(),
                        record.getStatus() != null && record.getStatus() == 1,
                        record.getPublishedAt() == null ? null : record.getPublishedAt().toString(),
                        record.getCreatedAt().toString()
                ));
    }

    @Transactional
    public AnnouncementDto create(long publisherId, CreateAnnouncementRequest request) {
        if (request.title() == null || request.title().isBlank()) {
            throw new ApiException("VALIDATION_ERROR", "标题不能为空", HttpStatus.BAD_REQUEST);
        }
        if (request.content() == null || request.content().isBlank()) {
            throw new ApiException("VALIDATION_ERROR", "内容不能为空", HttpStatus.BAD_REQUEST);
        }
        boolean publish = Boolean.TRUE.equals(request.publish_now());
        LocalDateTime now = LocalDateTime.now();
        Long id = dsl.insertInto(ANNOUNCEMENT)
                .set(ANNOUNCEMENT.TITLE, request.title().trim())
                .set(ANNOUNCEMENT.CONTENT, request.content().trim())
                .set(ANNOUNCEMENT.PUBLISHER_ID, publisherId)
                .set(ANNOUNCEMENT.STATUS, (byte) (publish ? 1 : 0))
                .set(ANNOUNCEMENT.PUBLISHED_AT, publish ? now : null)
                .set(ANNOUNCEMENT.CREATED_AT, now)
                .returningResult(ANNOUNCEMENT.ID)
                .fetchOne(ANNOUNCEMENT.ID);
        if (publish) {
            broadcastSystemNotification(request.title().trim(), request.content().trim());
        }
        return list().stream().filter(item -> item.id() == id).findFirst().orElseThrow();
    }

    private void broadcastSystemNotification(String title, String content) {
        var userIds = dsl.selectDistinct(NOTIFICATION.USER_ID).from(NOTIFICATION).fetch(NOTIFICATION.USER_ID);
        for (Long userId : userIds) {
            dsl.insertInto(NOTIFICATION)
                    .set(NOTIFICATION.USER_ID, userId)
                    .set(NOTIFICATION.TYPE, "system")
                    .set(NOTIFICATION.TITLE, title)
                    .set(NOTIFICATION.CONTENT, content)
                    .set(NOTIFICATION.IS_READ, (byte) 0)
                    .set(NOTIFICATION.CREATED_AT, LocalDateTime.now())
                    .execute();
        }
    }
}
