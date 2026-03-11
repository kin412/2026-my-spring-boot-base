package com.kin.base.domain.board.controller;

import com.kin.base.domain.board.dto.BoardDto;
import com.kin.base.domain.board.dto.BoardSearchCondition;
import com.kin.base.domain.board.service.BoardService;
import com.kin.base.domain.member.dto.MemberLoginDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/board")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/boardList")
    public String boardList(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable // 페이징 정보 (스프링 제공)
            , @ModelAttribute BoardSearchCondition boardSearchCondition
            , @SessionAttribute(name = "member", required = false) MemberLoginDto loginMember // 세션 정보 주입
            , Model model) {

        if(loginMember != null) {
            log.info("-=-로그인한 사용자 : " + loginMember.getLoginId());
            model.addAttribute("member", loginMember);
        }

        Page<BoardDto> boardList = boardService.findAll(pageable, boardSearchCondition);
        model.addAttribute("boardList", boardList);

        return "board/BoardList";
    }

}
