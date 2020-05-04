package com.hwanchang.todo.web;

import com.hwanchang.todo.domain.member.Member;
import com.hwanchang.todo.domain.member.MemberRepository;
import com.hwanchang.todo.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
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

    // POST 테스트 시 CSRF 토큰
    String TOKEN_ATTR_NAME = "org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN";
    HttpSessionCsrfTokenRepository httpSessionCsrfTokenRepository = new HttpSessionCsrfTokenRepository();
    CsrfToken csrfToken = httpSessionCsrfTokenRepository.generateToken(new MockHttpServletRequest());

    @Test
    void 회원가입_테스트() throws Exception {
        //when
        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/join/process")
                .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                .param(csrfToken.getParameterName(), csrfToken.getToken())
                .contentType(APPLICATION_FORM_URLENCODED)
                .param("email", "join@gmail.com")
                .param("name", "박환창")
                .param("password", "P@ssword!"))
                .andExpect(redirectedUrl("/login"))
                .andReturn();

        //then
        Optional<Member> userEntityWrapper = memberRepository.findByEmail("join@gmail.com");
        Member userEntity = userEntityWrapper.get();
        log.info("E-mail : {}", userEntity.getEmail());
        log.info("Name : {}", userEntity.getName());

        assertThat(userEntity.getEmail()).contains("join@gmail.com");
        assertThat(userEntity.getName()).contains("박환창");
    }

    @Test
    void 로그인_테스트() throws Exception {
        //when
        MvcResult result_join = mockMvc.perform(MockMvcRequestBuilders.post("/join/process")
                .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                .param(csrfToken.getParameterName(), csrfToken.getToken())
                .contentType(APPLICATION_FORM_URLENCODED)
                .param("email", "login@gmail.com")
                .param("name", "박환창")
                .param("password", "P@ssword!"))
                .andReturn();

        MvcResult result_login = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                .param(csrfToken.getParameterName(), csrfToken.getToken())
                .contentType(APPLICATION_FORM_URLENCODED)
                .param("username", "login@gmail.com")
                .param("password", "P@ssword!"))
                .andExpect(redirectedUrl("/"))
                .andReturn();
    }

    @Test
    void Email_중복_체크_테스트() throws Exception {
        //when
        MvcResult result_fail = mockMvc.perform(MockMvcRequestBuilders.post("/check/duplication")
                .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                .param(csrfToken.getParameterName(), csrfToken.getToken())
                .contentType(APPLICATION_JSON)
                .param("email", "duplication@gmail.com"))
                .andReturn();

        //then
        String response_fail = result_fail.getResponse().getContentAsString();
        log.info("Success (해당 Email 로 회원 가입 가능) - Response Content : " + response_fail);
        assertThat(response_fail).isEqualTo("false");

        //when
        mockMvc.perform(MockMvcRequestBuilders.post("/join/process")
                .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                .param(csrfToken.getParameterName(), csrfToken.getToken())
                .contentType(APPLICATION_FORM_URLENCODED)
                .param("email", "duplication@gmail.com")
                .param("name", "박환창")
                .param("password", "P@ssword!"))
                .andExpect(redirectedUrl("/login"))
                .andReturn();

        MvcResult result_success = mockMvc.perform(MockMvcRequestBuilders.post("/check/duplication")
                .sessionAttr(TOKEN_ATTR_NAME, csrfToken)
                .param(csrfToken.getParameterName(), csrfToken.getToken())
                .contentType(APPLICATION_JSON)
                .param("email", "duplication@gmail.com"))
                .andReturn();

        //then
        String response_success = result_success.getResponse().getContentAsString();
        log.info("Fail (해당 Email 로 회원 가입 불가) - Response Content : " + response_success);
        assertThat(response_success).isEqualTo("true");

    }

}