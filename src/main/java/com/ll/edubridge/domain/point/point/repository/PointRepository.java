package com.ll.edubridge.domain.point.point.repository;

import com.ll.edubridge.domain.point.point.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, Long> {
    List<Point> findByOwnerId(Long ownerId);

    List<Point> findByOwnerIdAndContent(Long memberId, String content);
}
