package com.hwanchang.todo.service;

import com.hwanchang.todo.domain.member.Member;
import com.hwanchang.todo.domain.member.MemberRepository;
import com.hwanchang.todo.domain.role.Role;
import com.hwanchang.todo.domain.role.RoleRepository;
import com.hwanchang.todo.dto.member.MemberSaveRequestDto;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {

    private MemberRepository memberRepository;
    private RoleRepository roleRepository;

    @Transactional
    public Long joinUser(MemberSaveRequestDto memberSaveRequestDto) {
        // 비밀번호 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        memberSaveRequestDto.setPassword(passwordEncoder.encode(memberSaveRequestDto.getPassword()));

        List<Role> role = new ArrayList<>();
        String email = memberSaveRequestDto.getEmail();

        if(email.equalsIgnoreCase("ghksckd219@gmail.com")) {
            role.add(new Role(email, Role.RoleType.ADMIN));
        } else {
            role.add(new Role(email, Role.RoleType.USER));
        }
        memberSaveRequestDto.setRoles(role);

        return memberRepository.save(memberSaveRequestDto.toEntity()).getMemberId();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Member> userEntityWrapper = memberRepository.findByEmail(email);
        Member userEntity = userEntityWrapper.get();

        List<GrantedAuthority> authorities = new ArrayList<>();

        // 사용자 Role 조회
        Optional<Role> roleEntityWrapper = roleRepository.findByEmail(email);
        Role roleEntity = roleEntityWrapper.get();
        authorities.add(new SimpleGrantedAuthority(roleEntity.getRole().getValue()));

        return new User(userEntity.getEmail(), userEntity.getPassword(), authorities);
    }
}