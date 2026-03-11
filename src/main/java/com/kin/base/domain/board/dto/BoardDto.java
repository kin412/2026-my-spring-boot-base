package com.kin.base.domain.board.dto;

import com.kin.base.domain.board.entity.Board;
import com.kin.base.domain.member.entity.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 255, message = "제목은 255자 이내여야 합니다.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 500, message = "내용은 500자 이내여야 합니다.")
    private String content;

    @NotBlank(message = "작성자를 입력해주세요.")
    private String author;

    private String createdDate;
    private String lastModifiedDate;

    @Builder
    public BoardDto(Board board) {
        this.author = board.getAuthor().getLoginId();
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
