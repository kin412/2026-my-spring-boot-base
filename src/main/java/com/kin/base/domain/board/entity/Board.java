package com.kin.base.domain.board.entity;

import com.kin.base.domain.member.entity.Member;
import com.kin.base.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

    //영속성 전이는 부모가 하나일때만, 그리고 부모객체가 사라질때 더이상 자식객체가 필요없는 경우만
    @OneToMany(mappedBy = "board"
            , cascade = CascadeType.ALL // 부모 저장/삭제 시 자식도 함께 저장/삭제
            , orphanRemoval = true) // 부모 리스트에서 자식 객체 제거 시 DB에서도 삭제
    private List<BoardFile> boardFiles = new ArrayList<>();

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
