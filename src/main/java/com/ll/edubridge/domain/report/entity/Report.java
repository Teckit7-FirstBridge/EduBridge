package com.ll.edubridge.domain.report.entity;


import com.ll.edubridge.global.jpa.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Setter
@Getter
@ToString(callSuper = true)
public class Report extends BaseEntity {

    @Column(length = 500)
    private String reportReason;

    @Enumerated(EnumType.STRING)
    private ReportType reportType;

    @Column
    private Long materialId;
}
