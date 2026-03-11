package com.kin.base.domain.defaultBoard.dto;

import com.kin.base.domain.defaultBoard.entity.DefaultBoard;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Getter
@Setter
@NoArgsConstructor //modalattribute를 쓰려면 내부적으로 생성되어야하므로  빈생성자를 뚫어줌.
public class DefaultBoardDto {
    
    private Long id;
    @NotBlank(message = "제목은 필수입니다.")
    @Size(max = 100, message = "제목은 100자 이내여야 합니다.")
    private String title;

    @NotBlank(message = "내용을 입력해주세요.")
    @Size(max = 500, message = "내용은 500자 이내여야 합니다.")
    private String content;

    @NotBlank(message = "작성자를 입력해주세요.")
    private String author;

    private String createdDate;
    private String lastModifiedDate;

    //엔티티 받아오는 생성자
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
