package com.kin.base.domain.defaultBoard.controller;

import com.kin.base.domain.defaultBoard.dto.DefaultBoardDto;
import com.kin.base.domain.defaultBoard.service.DefaultBoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class DefaultBoardController {

    private final DefaultBoardService defaultBoardService;

    @RequestMapping("/")
    public String defaultBoardList(Model model) {

        List<DefaultBoardDto> boardList = defaultBoardService.findAll();
        model.addAttribute("boardList", boardList);

        return "defaultBoard/defaultBoardList";
    }

}
