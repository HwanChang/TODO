package com.hwanchang.todo.config;

import com.hwanchang.todo.domain.role.Role;
import com.hwanchang.todo.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private MemberService memberService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/**");
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers("/admin/**").hasRole(Role.RoleType.ADMIN.getValue())
                .antMatchers("/h2-console/**").permitAll()
                .antMatchers("/signin").permitAll()
                .antMatchers("/signup").permitAll()
                .antMatchers("/signupprocess").permitAll()
                .antMatchers("/**").hasAnyRole(Role.RoleType.ADMIN.getValue(), Role.RoleType.USER.getValue())
//                .antMatchers("/**").permitAll()
        .and()
            .csrf().ignoringAntMatchers("/h2-console/**")
        .and()
            .headers().frameOptions().disable()
        .and()
            .formLogin()
            .loginPage("/signin")
            .defaultSuccessUrl("/")
        .and()
            .logout()
            .invalidateHttpSession(true)
            .permitAll()
		;
	}

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
    }
}