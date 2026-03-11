package com.kin.base.domain.member.controller;

import com.kin.base.domain.member.dto.MemberLoginDto;
import com.kin.base.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping({"/", "/member"})
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/")
    public String loginForm(Model model) {

        return "member/loginForm";
    }

    @PostMapping("/")
    public String login(@ModelAttribute MemberLoginDto memberLoginDto, Model model,
                        RedirectAttributes redirectAttributes, HttpServletRequest request) {

        try {

            MemberLoginDto mld = memberService.login(memberLoginDto);

            //로그인 세션
            HttpSession session = request.getSession();
            session.setAttribute("member", mld);

            return "redirect:/board/boardList";
        }catch (Exception e) {
            log.info("로그인 실패! e.getMessage() : " + e.getMessage());
            model.addAttribute("errorMessage",e.getMessage());
            //return "member/loginForm";

            // 리다이렉트 주소로 데이터를 "Flash"하게 보냄 (한 번 쓰고 사라짐)
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/";
        }

    }



}
