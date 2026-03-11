package com.kin.base.domain.member.controller;

import com.kin.base.domain.member.dto.MemberLoginDto;
import com.kin.base.domain.member.service.MemberService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

        model.addAttribute("member", new MemberLoginDto());
        return "member/loginForm";
    }

    @PostMapping("/")
    public String login(@Valid @ModelAttribute("member") MemberLoginDto memberLoginDto
            , BindingResult bindingResult
            , Model model
            , RedirectAttributes redirectAttributes
            , HttpServletRequest request) {

        // 1. 입력 값 자체에 오류가 있는 경우 (예: 아이디 미입력)
        if (bindingResult.hasErrors()) {
            log.info("errors={}", bindingResult);
            return "member/loginForm"; // 에러가 있으면 다시 작성 화면으로
        }

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

    @PostMapping("/logout")
    public String logout(HttpServletRequest request) {
        // 세션이 있으면 가져오고 없으면 null 반환 (false 옵션)
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate(); // 세션 전체를 무효화 (데이터 삭제)
        }

        return "redirect:/"; // 로그아웃 후 다시 로그인 페이지나 메인으로 이동
    }

}
