package com.kin.base.domain.defaultBoard.dto;

import com.kin.base.domain.defaultBoard.entity.DefaultBoard;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Getter
public class DefaultBoardDto {

    private Long id;
    private String title;
    private String content;
    private String author;

    private String createdDate;
    private String lastModifiedDate;

    public DefaultBoardDto(DefaultBoard defaultBoard) {
        this.id = defaultBoard.getId();
        this.title = defaultBoard.getTitle();
        this.content = defaultBoard.getContent();
        this.author = defaultBoard.getAuthor();

        // 💡 형식에 맞춰서 수정
        this.createdDate = defaultBoard.getCreatedDate().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        );
        this.lastModifiedDate = defaultBoard.getLastModifiedDate().format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        );

    }
}
