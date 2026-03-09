package com.kin.base.domain.defaultBoard.entity;

import com.kin.base.global.common.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter // 조회는 어디서든 필요하므로 사용
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 함부로 new 생성을 못하게 막음. jpa에서 쓰기위해 존재하긴 해야함. 그래서 protected
public class DefaultBoard extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String content;
    private String author;

    @Builder
    public DefaultBoard(String author, String content, Long id, String title) {
        this.author = author;
        this.content = content;
        this.id = id;
        this.title = title;
    }

    //BaseTimeEntity
    /*private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;*/

}
