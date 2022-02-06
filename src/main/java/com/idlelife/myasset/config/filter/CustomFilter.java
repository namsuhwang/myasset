package com.idlelife.myasset.config.filter;

import com.idlelife.myasset.common.auth.AuthProvider;
import com.idlelife.myasset.common.auth.CustomUserDetails;
import com.idlelife.myasset.common.exception.MyassetException;
import com.idlelife.myasset.models.common.ErrorCode;
import com.idlelife.myasset.models.member.MemberSearch;
import com.idlelife.myasset.models.member.entity.MemberTokenEntity;
import com.idlelife.myasset.service.AuthService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import static com.idlelife.myasset.models.common.ErrorCode.FORBIDDEN_EXCEPTION;

@Slf4j
@RequiredArgsConstructor
public class CustomFilter extends GenericFilterBean {
    @Autowired
    AuthService authService;

    private AuthProvider jwtTokenProvider;

    @Value("${jwt.secret.signature}")
    private String signatureKey;

    public CustomFilter(AuthProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest)request;
        HttpServletResponse httpRes = (HttpServletResponse) response;

       try {
            if("OPTIONS".equalsIgnoreCase(httpReq.getMethod())) {
                httpRes.setStatus(HttpServletResponse.SC_OK);
            } else {
            	/** 사용자 인증토큰 검사 */
                String token = jwtTokenProvider.resolveToken(httpReq);
                if (token != null) {
                    if(jwtTokenProvider.validateToken(token)) {
                        Authentication auth = jwtTokenProvider.getAuthentication(token);
                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }else {
                        log.info("getAuthentication 토큰 만료");
                        //throw new MyassetException(ErrorCode.AUTHENTICATION_ENTRY_POINT_EXCEPTION);
                    }
                }
                filterChain.doFilter(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}