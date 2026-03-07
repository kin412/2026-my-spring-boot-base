package com.kin.base.global.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class) // 자동으로 날짜를 매핑해줌
public class BaseTimeEntity {

    @CreatedDate // 생성 시 자동 저장
    // 생성 후 수정불가 및 LocalDateTime 초단위 자르기
    // 하지만 주석친 방법은 h2만 먹힘. dbms별로 다르게 넣어주는 옵션 precision
    // 실무에서는 아예 안넣기도 함.
    //@Column(updatable = false, columnDefinition = "TIMESTAMP(0)")
    @Column(updatable = false, precision = 0)
    private LocalDateTime createdDate;

    @LastModifiedDate // 수정 시 자동 저장
    @Column(precision = 0)
    private LocalDateTime lastModifiedDate;

}
