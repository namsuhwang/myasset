package com.idlelife.myasset.config.filter;

import com.idlelife.myasset.common.CommonUtil;
import com.idlelife.myasset.common.auth.AuthProvider;
import com.idlelife.myasset.common.auth.CustomUserDetails;
import com.idlelife.myasset.common.exception.MyassetException;
import com.idlelife.myasset.models.common.ErrorCode;
import com.idlelife.myasset.models.member.MemberSearch;
import com.idlelife.myasset.models.member.entity.MemberRoleEntity;
import com.idlelife.myasset.repository.AuthMapper;
import com.idlelife.myasset.service.AuthService;
import com.idlelife.myasset.service.MemberService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@NoArgsConstructor
//@RequiredArgsConstructor
@Component
public class CustomFilter extends GenericFilterBean {
    //private AuthMapper authMapper;
    @Autowired
    AuthMapper authMapper;

    @Value("${jwt.secret.signature}")
    private String signatureKey;

    private static final String BEARER_TYPE = "bearer ";
    private static final String AUTHORITIES_KEY = "auth";
    private static final String ACCESS_USER_ID = "memberId";

//    private AuthProvider authProvider;

//    private String signatureKey;
//
//    public CustomFilter(AuthMapper authMapper, String signatureKey) {
//        this.authMapper = authMapper;
//        this.signatureKey = signatureKey;
//    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest)request;
        HttpServletResponse httpRes = (HttpServletResponse) response;

//        AuthService authService = new AuthService();
       try {
            if("OPTIONS".equalsIgnoreCase(httpReq.getMethod())) {
                httpRes.setStatus(HttpServletResponse.SC_OK);
            } else {
            	/** 사용자 인증토큰 검사 */
                if (httpReq.getHeader("accesstoken") != null
                        && !httpReq.getHeader("accesstoken").equalsIgnoreCase("null")
                        && !httpReq.getHeader("accesstoken").equalsIgnoreCase("undefined"))
                {
                    String token = httpReq.getHeader("accesstoken").replace(BEARER_TYPE, "");
                    if (token != null && validateToken(token)) {
                            Authentication auth = getAuthentication(token);
                            SecurityContextHolder.getContext().setAuthentication(auth);
                    } else {
                        log.info("doFilter getAuthentication 토큰 만료");
                        //throw new MyassetException(ErrorCode.AUTHENTICATION_ENTRY_POINT_EXCEPTION);
                    }
                }
                filterChain.doFilter(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * @method 설명 : 토큰 유효시간 만료여부 검사 실행
     */
    public boolean validateToken(String token) {
        try {
            if(CommonUtil.getSignatureKey() == null){
                log.error("validateToken CommonUtil signatureKey is null");
            }else{
                log.error("validateToken CommonUtil signatureKey is not null : " + signatureKey);
            }

            if(this.signatureKey == null){
                log.error("validateToken signatureKey is null");
            }else{
                log.error("validateToken signatureKey is not null : " + signatureKey);
            }
            Jws<Claims> claims = Jwts.parser().setSigningKey(this.signatureKey.getBytes()).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @method 설명 : 컨텍스트에 해당 유저에 대한 권한을 전달하고 API 접근 전 접근 권한을 확인하여 접근 허용 또는 거부를 진행한다.
     */
    @SuppressWarnings("unchecked")
    public Authentication getAuthentication(String token) {
        Claims claims = null;
        // 토큰 기반으로 유저의 정보 파싱
        try{
            claims = Jwts.parser().setSigningKey(signatureKey.getBytes()).parseClaimsJws(token).getBody();
        }catch(ExpiredJwtException e){
            log.info("getAuthentication 토큰 만료");
            throw new MyassetException(ErrorCode.AUTHENTICATION_ENTRY_POINT_EXCEPTION);
        }

        String username = claims.getSubject();
        long id = claims.get(ACCESS_USER_ID, Long.class);
//      String roles = claims.get(AUTHORITIES_KEY, String.class);

        MemberSearch param = new MemberSearch();
        param.setMemberId(id);
        param.setEmail(username);
        List<String> roles  = new ArrayList<>();
        List<MemberRoleEntity>  roleList = authMapper.selectMemberRoleList(param);
        for(MemberRoleEntity roleEntity : roleList) {
            roles.add("ROLE_" + roleEntity.getRoleCd());
        }
        CustomUserDetails userDetails = new CustomUserDetails(id, username, roles);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }
}