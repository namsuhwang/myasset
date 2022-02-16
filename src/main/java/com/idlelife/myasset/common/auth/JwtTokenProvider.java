package com.idlelife.myasset.common.auth;

import com.idlelife.myasset.models.member.dto.MemberAuthDto;
import com.idlelife.myasset.service.AuthService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {
    @Autowired
    AuthService authService;


    private static final String BEARER_TYPE = "bearer ";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 10;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 365;

    @Value("${jwt.secret.signature}")
    private String signatureKey;

    @PostConstruct
    protected void init() {
        signatureKey = Base64.getEncoder().encodeToString(signatureKey.getBytes());
    }


    public String createAccessToken(String userPk){
        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

        final JwtBuilder builder = Jwts.builder()
                .setSubject(userPk)
                .setExpiration(accessTokenExpiresIn)
                .signWith(SignatureAlgorithm.HS256, signatureKey);

        String accessToken = BEARER_TYPE + builder.compact();
        log.info("엑세스 토큰 발급 : " + accessToken);
        return accessToken;
    }

    public String createRefreshToken(String userPk){
        long now = (new Date()).getTime();
        Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        final JwtBuilder builder = Jwts.builder()
                .setSubject(userPk)
                .setExpiration(refreshTokenExpiresIn)
                .signWith(SignatureAlgorithm.HS256, signatureKey);

        String refreshToken =  BEARER_TYPE + builder.compact();
        log.info("리프레쉬 토큰 발급 : " + refreshToken);
        return refreshToken;
    }



    // 토큰에서 회원 정보 추출(userPk(email))
    public String getUserPk(String token) {
        String userPk = getClaims(token).getBody().getSubject();
        log.info("getUserPk : " + userPk);
        return userPk;
    }

    /**
     * @method 설명 : 컨텍스트에 해당 유저에 대한 권한을 전달하고 API 접근 전 접근 권한을 확인하여 접근 허용 또는 거부를 진행한다.
     */
    @SuppressWarnings("unchecked")
    public Authentication getAuthentication(String token) {
        MemberAuthDto memberAuthDto = authService.getMemberAuth(getUserPk(token));

        CustomUserDetails userDetails = new CustomUserDetails(memberAuthDto.getMemberId(), memberAuthDto.getEmail(), memberAuthDto.getRoles());
        Authentication auth = new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
        log.info("getAuthentication : " + auth.toString());
        return auth;
    }

    /**
     * @method 설명 : request객체 헤더에 담겨 있는 토큰 가져오기
     */
    public String resolveToken(HttpServletRequest request) {
        if (request.getHeader("accesstoken") == null
                || request.getHeader("accesstoken").equalsIgnoreCase("null")
                || request.getHeader("accesstoken").equalsIgnoreCase("undefined"))
            return null;

        return request.getHeader("accesstoken").replace(BEARER_TYPE, "");
    }

    /**
     * @method 설명 : request객체 헤더에 담겨 있는 리프레쉬 토큰 가져오기
     */
    public String resolveRefreshToken(HttpServletRequest request) {
        if (request.getHeader("refreshtoken") == null)
            return null;

        return request.getHeader("refreshtoken").replace(BEARER_TYPE, "");
    }

    /**
     * @method 설명 : 토큰 유효시간 만료여부 검사 실행
     */
    public boolean validateToken(String token) {
        try {
            Jws<Claims> claims = getClaims(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e){
            log.error("validateToken 토큰 만료 : " + token);
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @method 설명 : 리프레쉬 토큰 유효시간 만료여부 검사 실행
     */
    public boolean validateRefreshToken(String refreshToken) {
        try {
            Jws<Claims> claims = getClaims(refreshToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e){
            log.error("validateRefreshToken 리프레쉬 토큰 만료 : " + refreshToken);
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    // 토큰 만료 일시 조회
    public LocalDateTime getTokenExpireDatetime(String token){
        Jws<Claims> claims = getClaims(token);
        LocalDateTime tokenExpireDatetime = claims.getBody().getExpiration().toInstant() // Date -> Instant
                .atZone(ZoneId.systemDefault()) // Instant -> ZonedDateTime
                .toLocalDateTime();
        return tokenExpireDatetime;
    }

    public Jws<Claims> getClaims(String token){
        String netToken = token.startsWith(BEARER_TYPE) ? token.replace(BEARER_TYPE, "") : token;
        Jws<Claims> claims = Jwts.parser().setSigningKey(signatureKey).parseClaimsJws(netToken);
        return claims;
    }
//
//    public String getRefreshTokenByMember(MemberEntity member){
////        Authentication auth = getAuthentication(token);
////        Long memberId = ((CustomUserDetails)auth.getPrincipal()).getId();
////        String email = ((CustomUserDetails)auth.getPrincipal()).getUsername();
//        MemberSearch memberSearch = new MemberSearch();
//        memberSearch.setEmail(member.getEmail());
//        memberSearch.setMemberId(member.getMemberId());
//        String refreshToken = "";
//        MemberTokenEntity tokenInfo = getMemberToken(memberSearch);
//        if(tokenInfo == null){
//            refreshToken = createRefreshToken();
//            tokenInfo = new MemberTokenEntity();
//            tokenInfo.setRefreshToken(refreshToken);
//            tokenInfo.setMemberId(member.getMemberId());
//            tokenInfo.setDeleteYn("N");
//            Jws<Claims> claims = getClaims(refreshToken);
//            LocalDateTime refreshTokenExpireDatetime = claims.getBody().getExpiration().toInstant() // Date -> Instant
//                    .atZone(ZoneId.systemDefault()) // Instant -> ZonedDateTime
//                    .toLocalDateTime();
//            tokenInfo.setRefreshTokenExpireDatetime(refreshTokenExpireDatetime);
//            regMemberToken(tokenInfo);
//            log.info("리프레쉬 토큰 신규 등록 완료;");
//        }else{
//            refreshToken = tokenInfo.getRefreshToken();
//            if(tokenInfo.getRemainingRefreshTokenSeconds() <= (60 * 60 * 24 * 30)){
//                refreshToken = createRefreshToken();
//                Jws<Claims> claims = getClaims(refreshToken);
//                LocalDateTime refreshTokenExpireDatetime = claims.getBody().getExpiration().toInstant() // Date -> Instant
//                        .atZone(ZoneId.systemDefault()) // Instant -> ZonedDateTime
//                        .toLocalDateTime();
//                tokenInfo.setMemberId(member.getMemberId());
//                tokenInfo.setDeleteYn("N");
//                tokenInfo.setRefreshToken(refreshToken);
//                tokenInfo.setRefreshTokenExpireDatetime(refreshTokenExpireDatetime);
//                modMemberToken(tokenInfo);
//                log.info("리프레쉬 토큰 새로 발급하여 연장 등록 완료");
//            }else{
//                log.info("리프레쉬 토큰 유지");
//            }
//        }
//
//        return refreshToken;
//    }
}
