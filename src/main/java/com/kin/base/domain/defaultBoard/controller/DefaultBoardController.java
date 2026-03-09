package com.kin.base.domain.defaultBoard.controller;

import com.kin.base.domain.defaultBoard.dto.DefaultBoardDto;
import com.kin.base.domain.defaultBoard.dto.DefaultBoardSearchCondition;
import com.kin.base.domain.defaultBoard.entity.DefaultBoard;
import com.kin.base.domain.defaultBoard.service.DefaultBoardService;
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

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class DefaultBoardController {

    private final DefaultBoardService defaultBoardService;

    @GetMapping("/")
    public String defaultBoardList(@PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable // 페이징 정보 (스프링 제공)
                                   //@RequestParam(value = "page", defaultValue = "0") int page
            , @ModelAttribute DefaultBoardSearchCondition defaultBoardSearchCondition
            , Model model) {

        //log.info("-=- defaultBoardSearchCondition.toString() : " + defaultBoardSearchCondition.toString());
        Page<DefaultBoardDto> boardList = defaultBoardService.findAll(pageable, defaultBoardSearchCondition);
        model.addAttribute("boardList", boardList);

        return "defaultBoard/defaultBoardList";
    }

    @GetMapping("/{id}")
    public String defaultBoardDetail(@PathVariable Long id, Model model) {

        model.addAttribute("board", defaultBoardService.findById(id));

        return "defaultBoard/defaultBoardDetail";
    }

    @GetMapping("/new")
    public String defaultBoardNewForm(Model model) {

        model.addAttribute("board", DefaultBoard.builder()
                .title("")
                .author("")
                .content("")
                .build());

        return "defaultBoard/defaultBoardForm";
    }

    @PostMapping("/save")
    public String defaultBoardSave(@Valid @ModelAttribute("board") DefaultBoardDto defaultBoardDto, BindingResult result) {

        if (result.hasErrors()) {
            log.info("errors={}", result);
            return "defaultBoard/defaultBoardForm"; // 에러가 있으면 다시 작성 화면으로
        }
        return "redirect:/"+defaultBoardService.save(defaultBoardDto);
    }

    @GetMapping("/edit/{id}")
    public String defaultBoardEditForm(@PathVariable Long id, Model model) {

        model.addAttribute("board", defaultBoardService.findById(id));

        return "defaultBoard/defaultBoardForm";
    }

    @PostMapping("/update/{id}")
    public String defaultBoardUpdate(@Valid @ModelAttribute("board") DefaultBoardDto defaultBoardDto, BindingResult result) {

        if (result.hasErrors()) {
            log.info("errors={}", result);
            return "defaultBoard/defaultBoardForm"; // 에러가 있으면 다시 작성 화면으로
        }

        return "redirect:/"+defaultBoardService.update(defaultBoardDto);
    }

    @DeleteMapping("/delete/{id}")
    //@ResponseBody
    public ResponseEntity<String> defaultBoardDeleteById(@PathVariable Long id) {

        defaultBoardService.deleteById(id);

        return ResponseEntity.ok("ok");
    }

}
