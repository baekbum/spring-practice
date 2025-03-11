package com.example.demo.web.auth.filter;

import com.example.demo.web.auth.service.CustomUserDetailsService;
import com.example.demo.web.auth.util.JwtTokenUtil;
import com.example.demo.web.member.exception.NoSearchMemberException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // 토큰을 발급 받는 프로세스
        // 요청 경로가 /authenticate인 경우 JWT 필터를 건너뜀
        if ("/authenticate".equals(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        // 유효한 토큰을 가지고 있을 때 동작하는 프로세스
        try {
            String authorizationHeader = request.getHeader("J-TOKEN");

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) throw new IllegalArgumentException("토큰 정보가 올바르지 않습니다.");

            String jwtToken = authorizationHeader.substring(7);
            String username = jwtTokenUtil.getUsernameFromToken(jwtToken);

            if (username == null) throw new IllegalArgumentException("토큰에서 이름을 추출할 수 없습니다.");

            UserDetails details = userDetailsService.loadUserByUsername(username);
            validToken(jwtToken, details);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("text/html; charset=UTF-8");
            response.getWriter().write("올바른 토큰이 아닙니다.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 토큰을 검증하고 보안 컨텍스트에 정보를 저장.
     * @param token
     * @param details
     */
    private void validToken(String token, UserDetails details) {
        if (jwtTokenUtil.validToken(token, details)) {
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(details.getUsername(), details.getPassword(), details.getAuthorities());
            usernamePasswordAuthenticationToken.setDetails(details);
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        } else {
            throw new IllegalArgumentException("토큰 정보가 올바르지 않습니다.");
        }
    }
}

