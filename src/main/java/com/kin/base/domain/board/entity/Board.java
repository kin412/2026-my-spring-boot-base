package com.kin.base.domain.board.entity;

import com.kin.base.domain.member.entity.Member;
import com.kin.base.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "BOARD")
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "TYPE", nullable = false, length = 20)
    private String type;

    @Column(name = "TITLE", nullable = false, length = 255)
    private String title;

    @Lob
    @Column(name = "CONTENT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AUTHOR")
    private Member author;

    @Builder
    public Board(String content, String title, String type) {
        this.content = content;
        this.title = title;
        this.type = type;
    }

    //setter가 나쁜게 아니다. 정확한 의도가 있는 메서드를 만들어 사용하라.
    public void updateBoard(String content, String title, String type) {
        this.content = content;
        this.title = title;
        this.type = type;
    }

    //연관관계 편의 메서드
    public void changeAuthor(Member author) {
        this.author = author;

        if (author != null && !author.getBoards().contains(this)) {
            author.getBoards().add(this);
        }

    }


}
