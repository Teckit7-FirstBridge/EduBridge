package com.ll.edubridge.domain.point.point.service;

import com.ll.edubridge.domain.member.member.entity.Member;
import com.ll.edubridge.domain.point.point.entity.Point;
import com.ll.edubridge.domain.point.point.entity.PointType;
import com.ll.edubridge.domain.point.point.repository.PointRepository;
import com.ll.edubridge.global.rq.Rq;
import com.ll.edubridge.global.sse.NotificationService;
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

    @Transactional
    public Point addPoint(PointType pointType, Member member) {
        Point point = Point.builder()
                .content(pointType.getContent())
                .ownerId(member.getId())
                .amount(pointType.getAmount())
                .build();
        return pointRepository.save(point);
    }

    public List<Point> findByOwnerId(Long memberId) {
        return pointRepository.findByOwnerId(memberId);
    }

}