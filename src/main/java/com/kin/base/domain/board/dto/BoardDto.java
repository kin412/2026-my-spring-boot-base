package com.kin.base.domain.board.dto;

import com.kin.base.domain.board.entity.Board;
import com.kin.base.domain.member.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class BoardDto {

    private Long id;
    private String type;
    private String title;
    private String content;
    private Member author;

    private String createdDate;
    private String lastModifiedDate;

    @Builder
    public BoardDto(Board board) {
        this.author = board.getAuthor();
        this.content = board.getContent();
        this.id = board.getId();
        this.title = board.getTitle();
        this.type = board.getType();

        // 💡 형식에 맞춰서 수정
        this.createdDate = board.getCreatedDate().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        );
        this.lastModifiedDate = board.getLastModifiedDate().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        );

    }
}
