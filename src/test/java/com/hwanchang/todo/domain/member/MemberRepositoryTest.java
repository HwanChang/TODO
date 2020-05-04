package com.hwanchang.todo.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void 멤버_정보_등록_테스트 () {
        //given
        LocalDateTime now = LocalDateTime.now();
        memberRepository.save(Member.builder()
                .email("join_test@gmail.com")
                .password("p@ssword!")
                .name("박환창")
                .build());
        //when
        List<Member> memberList = memberRepository.findAll();

        //then
        Member member = memberList.get(0);

        log.info(member.getEmail());
        log.info(member.getName());

        assertThat(member.getEmail()).isEqualTo("join_test@gmail.com");
        assertThat(member.getName()).isEqualTo("박환창");

        log.info(member.getCreatedDate().toString());
        log.info(member.getModifiedDate().toString());

        assertThat(member.getCreatedDate()).isAfter(now);
        assertThat(member.getModifiedDate()).isAfter(now);
    }
}
