package com.kin.base.domain.board.controller;

import com.kin.base.domain.board.dto.BoardDto;
import com.kin.base.domain.board.dto.BoardSearchCondition;
import com.kin.base.domain.board.repository.BoardRepository;
import com.kin.base.domain.board.service.BoardService;
import com.kin.base.domain.member.dto.MemberLoginDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

    @GetMapping("/{id}")
    public String boardDetail(@PathVariable Long id
            , @SessionAttribute(name = "member", required = false) MemberLoginDto loginMember
            , RedirectAttributes redirectAttributes
            , Model model) {

        try {

            model.addAttribute("board", boardService.findById(id));
            model.addAttribute("member", loginMember);

            return "board/BoardDetail";
        } catch (Exception e) {
            log.info("-=-게시글 상세조회 실패! e.getMessage() : " + e.getMessage());
            model.addAttribute("errorMessage",e.getMessage());

            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/board/boardList";
        }

    }

    @GetMapping("/new")
    public String newBoard(@SessionAttribute(name = "member", required = false) MemberLoginDto loginMember
                           , Model model) {

        BoardDto boardDto = new BoardDto();

        if(loginMember != null) {
            //model.addAttribute("member", loginMember);
            boardDto.setAuthor(loginMember.getLoginId());
        }

        model.addAttribute("formMode", "new");
        model.addAttribute("board", boardDto);

        return "board/boardForm";
    }

    @PostMapping("/save")
    public String save(@Valid @ModelAttribute("board") BoardDto boardDto
            , BindingResult bindingResult){

        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "board/boardForm"; // 에러가 있으면 다시 작성 화면으로
        }

        Long id = boardService.save(boardDto);

        return "redirect:/board/"+id;
    }

    @GetMapping("/edit/{id}")
    public String boardEditForm(@PathVariable Long id
            , @SessionAttribute(name = "member", required = false) MemberLoginDto loginMember
            , RedirectAttributes redirectAttributes
            , Model model) {

        BoardDto boardDto = boardService.findById(id);

        if(loginMember.getLoginId().equals(boardDto.getAuthor())) {
            model.addAttribute("formMode", "update");
            model.addAttribute("board", boardDto);
            model.addAttribute("member", loginMember);

            return "board/boardForm";
        }else{
            redirectAttributes.addFlashAttribute("errorMessage", "잘못된 접근입니다.");
            return "redirect:/board/boardList";
        }


    }

    @PostMapping("/update/{id}")
    public String updateBoard(@Valid @ModelAttribute("board") BoardDto boardDto
            , BindingResult bindingResult
            ,Model model){

        if(bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "board/boardForm";
        }

        return "redirect:/board/"+boardService.update(boardDto);

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long id) {

        boardService.delete(id);

        return ResponseEntity.ok("ok");

    }

}
