package com.example.demo.web.auth.service;

import com.example.demo.web.auth.entity.User;
import com.example.demo.web.member.entity.Member;
import com.example.demo.web.member.exception.NoSearchMemberException;
import com.example.demo.web.member.repository.SpringJpaMember;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final SpringJpaMember memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //System.out.println("username = " + username);

        Member findMember = memberRepository.findByMemberId(username)
                .orElseThrow(() -> new NoSearchMemberException("사용자를 찾을 수 없습니다."));

        List<GrantedAuthority> authorities = new ArrayList<>();

        if (findMember.getMemberId().equals("admin")) {
            authorities = List.of(
                new SimpleGrantedAuthority("ROLE_ADMIN")
            );
        } else {
            authorities = List.of(
                    new SimpleGrantedAuthority("ROLE_USER")
            );
        }

        UserDetails details = User.builder()
                .username(findMember.getMemberId())
                .password(findMember.getPassword())
                .authorities(authorities)
                .build();

//        log.info("details.getUsername() = {}", details.getUsername());
//        log.info("details.getPassword() = {}", details.getPassword());
//        log.info("details.getAuthorities() = {}", details.getAuthorities());

        return details;
    }
}
