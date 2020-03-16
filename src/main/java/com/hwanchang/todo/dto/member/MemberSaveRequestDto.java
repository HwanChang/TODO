package com.hwanchang.todo.dto.member;

import com.hwanchang.todo.domain.member.Member;
import com.hwanchang.todo.domain.role.Role;
import lombok.*;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class MemberSaveRequestDto {

    private String email;
    private String password;
    private String name;
    private List<Role> roles;

    @Builder
    public MemberSaveRequestDto(String email, String password, String name, List<Role> roles) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.roles = roles;
    }

    public Member toEntity(){
        return Member.builder()
                .email(email)
                .password(password)
                .name(name)
                .roles(roles)
                .build();
    }
}
