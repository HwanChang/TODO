package com.hwanchang.todo.dto.member;

import com.hwanchang.todo.domain.member.Member;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class MemberSaveRequestDto {

    private String email;
    private String password;
    private String name;

    @Builder
    public MemberSaveRequestDto(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public Member toEntity(){
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .build();
    }
}
