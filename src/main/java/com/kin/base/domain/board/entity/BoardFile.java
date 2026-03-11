package com.kin.base.domain.board.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "BOARD_FILE")
public class BoardFile {

    @Id
    @GeneratedValue
    private Long id;
    private String originName;
    private String saveName;
    private String path;
    private Long size;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    @Builder
    public BoardFile(String originName, String path, String saveName, Long size) {
        this.originName = originName;
        this.path = path;
        this.saveName = saveName;
        this.size = size;

        /*if (board != null) {
            changeBoard(board); // 생성 시점에 연관관계 편의 메서드 호출
        }*/

    }

    public void changeBoard(Board board) {
        // 1. 기존에 이미 다른 board와 연결되어 있었다면, 그 board의 리스트에서 나를 제거.
        // file에서는 쓸일이 없지만 예시로. 객체그래프의 정합성을 위해 관례적으로 씀.
        /*if (this.board != null) {
            this.board.getBoardFiles().remove(this);
        }*/

        this.board = board;

        // 2. 새로운 board의 리스트에 나를 추가
        if (board != null && !board.getBoardFiles().contains(this)) {
            board.getBoardFiles().add(this);
        }

    }

}
