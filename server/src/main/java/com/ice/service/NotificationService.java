package com.ice.service;

import java.time.LocalDateTime;
import org.jooq.DSLContext;
import org.springframework.stereotype.Service;

import static com.ice.generated.jooq.tables.Notification.NOTIFICATION;

@Service
public class NotificationService {

    private final DSLContext dsl;

    public NotificationService(DSLContext dsl) {
        this.dsl = dsl;
    }

    public void sendAuditPass(long userId, long articleId, String articleTitle) {
        insert(userId, "audit_pass", "文章审核通过",
                "《" + articleTitle + "》已通过审核", "article", articleId);
    }

    public void sendAuditReject(long userId, long articleId, String articleTitle, String reason) {
        String content = "《" + articleTitle + "》未通过审核";
        if (reason != null && !reason.isBlank()) {
            content = content + "：" + reason;
        }
        insert(userId, "audit_reject", "文章审核未通过", content, "article", articleId);
    }

    private void insert(
            long userId,
            String type,
            String title,
            String content,
            String refType,
            long refId
    ) {
        dsl.insertInto(NOTIFICATION)
                .set(NOTIFICATION.USER_ID, userId)
                .set(NOTIFICATION.TYPE, type)
                .set(NOTIFICATION.TITLE, title)
                .set(NOTIFICATION.CONTENT, content)
                .set(NOTIFICATION.REF_TYPE, refType)
                .set(NOTIFICATION.REF_ID, refId)
                .set(NOTIFICATION.IS_READ, (byte) 0)
                .set(NOTIFICATION.CREATED_AT, LocalDateTime.now())
                .execute();
    }
}
