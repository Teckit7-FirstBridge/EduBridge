package com.ll.edubridge.domain.point.point.service;

import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.notification.service.NotificationService;
import com.ll.edubridge.domain.point.point.entity.Point;
import com.ll.edubridge.domain.point.point.entity.PointType;
import com.ll.edubridge.domain.point.point.repository.PointRepository;
import com.ll.edubridge.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PointService {
    private final PointRepository pointRepository;
    private final NotificationService notificationService;
    private final Rq rq;

    public void addPoint(PointType pointType, Member member) {
        Point point = Point.builder()
                .content(pointType.getContent())
                .ownerId(member.getId())
                .amount(pointType.getAmount())
                .build();
        pointRepository.save(point);
    }

    public void subPoint(PointType pointType, Member member, int price) {
        Point point = Point.builder()
                .content(pointType.getContent())
                .ownerId(member.getId())
                .amount(price*(-1))
                .build();
        pointRepository.save(point);
    }

    public List<Point> findByOwnerId(Long memberId) {
        return pointRepository.findByOwnerId(memberId);
    }

    public List<Point> findByOwnerIdAndContent(Long memberId){
        return pointRepository.findByOwnerIdAndContent(memberId, PointType.Attend.getContent());
    }

}