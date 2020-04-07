package com.hwanchang.todo.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hwanchang.todo.domain.member.Member;
import com.hwanchang.todo.domain.member.MemberRepository;
import com.hwanchang.todo.domain.role.Role;
import com.hwanchang.todo.dto.member.MemberSaveRequestDto;
import com.hwanchang.todo.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;

    // POST 테스트 시 CSRF 토큰
    String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
    HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
    CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

    @Test
    void 회원가입_페이지_로딩테스트() throws Exception {
        //when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        //then
        String dow = result.getResponse().getContentAsString();
        log.info("Response Content : " + dow);
        assertThat(dow).contains("회원가입 페이지");
    }

    @Test
    void 회원가입_테스트() throws Exception {
        //given
        List<Role> role = new ArrayList<>();
        role.add(new Role("ghksckd219@naver.com", Role.RoleType.USER));

        MemberSaveRequestDto memberSaveRequestDto = MemberSaveRequestDto.builder()
                .email("ghksckd219@naver.com")
                .name("박환창")
                .password("P@ssword!")
                .roles(role)
                .build();

        //when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/signupprocess")
                .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                .param(csrfToken.getParameterName(), csrfToken.getToken())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(memberSaveRequestDto))
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().is3xxRedirection()).andReturn();

        //then
        Optional<Member> userEntityWrapper = memberRepository.findByEmail("ghksckd219@naver.com");
        Member userEntity = userEntityWrapper.get();
        log.info("Name : {}", userEntity.getName());

        assertThat(userEntity.getName()).contains("박환창");
    }

}