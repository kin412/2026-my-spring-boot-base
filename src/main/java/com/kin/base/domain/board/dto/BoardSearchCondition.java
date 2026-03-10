package com.kin.base.domain.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class BoardSearchCondition {

    private String title;
    private String author;
    private String createdDate;
    /*@DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate;*/

}
