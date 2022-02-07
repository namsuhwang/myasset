package com.idlelife.myasset.config.filter;

import com.idlelife.myasset.common.auth.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomFilter extends OncePerRequestFilter {

    private JwtTokenProvider jwtTokenProvider;

    private static final String BEARER_TYPE = "bearer ";

    public CustomFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            if("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                /** 사용자 인증토큰 검사 */
                if (request.getHeader("accesstoken") != null
                        && !request.getHeader("accesstoken").equalsIgnoreCase("null")
                        && !request.getHeader("accesstoken").equalsIgnoreCase("undefined"))
                {
                    String token = request.getHeader("accesstoken").replace(BEARER_TYPE, "");
                    if (token != null && jwtTokenProvider.validateToken(token)) {
                        Authentication auth = jwtTokenProvider.getAuthentication(token);
                        SecurityContextHolder.getContext().setAuthentication(auth);
                        log.info("doFilter 완료");
                    }else{
                        log.error("토큰 인증 실패 : " + token);
                    }
                }
                filterChain.doFilter(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}