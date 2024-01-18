package com.ll.edubridge.domain.base.genfile.entity;

import com.ll.edubridge.global.jpa.entity.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.*;

import static lombok.AccessLevel.PROTECTED;

@Entity
@Builder
@AllArgsConstructor(access = PROTECTED)
@NoArgsConstructor(access = PROTECTED)
@Setter
@Getter
@ToString(callSuper = true)
@Table(
        uniqueConstraints = @UniqueConstraint(
                columnNames = {
                        "relId", "relTypeCode", "typeCode", "type2Code", "fileNo"
                }
        ),
        indexes = {
                // 특정 그룹의 데이터들을 불러올 때
                @Index(name = "GenFile_idx2", columnList = "relTypeCode, typeCode, type2Code")
        }
)
public class GenFile extends BaseEntity {

    private String relTypeCode;

    private long relId;

    private String typeCode;

    private String type2Code;

    private String fileExtTypeCode;

    private String fileExtType2Code;

    private long fileSize;

    private long fileNo;

    private String fileExt;

    private String fileDir;

    private String originFileName;

}
