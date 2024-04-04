package com.ll.edubridge.domain.notification.repository;

import com.ll.edubridge.domain.notification.entity.Notification;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ll.edubridge.domain.notification.entity.QNotification.notification;

@RequiredArgsConstructor
public class CustomNotificationRepositoryImpl implements CustomNotificationRepository{
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Notification> findByMemberId(Long memberId) {
        List<Notification> fetch = queryFactory.selectFrom(notification)
                .where(notification.recipient_id.eq(memberId))
                .orderBy(notification.createDate.desc())
                .limit(10)
                .fetch();

        return fetch;
    }
}
