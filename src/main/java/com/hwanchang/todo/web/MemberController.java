package com.hwanchang.todo.web;

import com.hwanchang.todo.dto.member.MemberSaveRequestDto;
import com.hwanchang.todo.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@AllArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원 가입 프로세스
    @PostMapping("/join/process")
    public String joinProcess( HttpServletRequest request, MemberSaveRequestDto memberSaveRequestDto ) {
        try {
            log.info("회원 가입 Email : {}", memberSaveRequestDto.getEmail());
            memberService.joinUser(memberSaveRequestDto);
        } catch (Exception e) {
            log.error(e.getMessage());
            String referer = request.getHeader("REFERER");
            return "redirect:" + referer;
        }
        return "redirect:/login";
    }

    // 회원 가입 Email 중복 체크
    @PostMapping("/check/duplication")
    @ResponseBody
    public String checkDuplication(@RequestParam(value = "email") String email) {
        log.info("Check Duplication Email : {}", email);
        /*
            중복 체크 결과
            true: 이미 존재 하는 Email
            false: 사용 가능한 Email
        */
        return Boolean.toString(memberService.checkUserDuplication(email));
    }

}