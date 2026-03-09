package com.kin.base.domain.defaultBoard.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@ToString
@AllArgsConstructor
public class DefaultBoardSearchCondition {

    private String title;
    private String author;
    private String createdDate;
    /*@DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate;*/

}
