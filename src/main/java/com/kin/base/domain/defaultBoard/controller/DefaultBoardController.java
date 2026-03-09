package com.kin.base.domain.defaultBoard.controller;

import com.kin.base.domain.defaultBoard.dto.DefaultBoardDto;
import com.kin.base.domain.defaultBoard.dto.DefaultBoardSearchCondition;
import com.kin.base.domain.defaultBoard.service.DefaultBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class DefaultBoardController {

    private final DefaultBoardService defaultBoardService;

    @RequestMapping("/")
    public String defaultBoardList(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable // 페이징 정보 (스프링 제공)
                                   //@RequestParam(value = "page", defaultValue = "0") int page
            , @ModelAttribute DefaultBoardSearchCondition defaultBoardSearchCondition
            , Model model) {

        log.info("-=- defaultBoardSearchCondition.toString() : " + defaultBoardSearchCondition.toString());
        Page<DefaultBoardDto> boardList = defaultBoardService.findAll(pageable, defaultBoardSearchCondition);
        model.addAttribute("boardList", boardList);

        return "defaultBoard/defaultBoardList";
    }

}
