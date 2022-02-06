package com.idlelife.myasset.common.auth;

import com.idlelife.myasset.common.exception.MyassetException;
import com.idlelife.myasset.models.common.ErrorCode;
import com.idlelife.myasset.models.member.MemberSearch;
import com.idlelife.myasset.models.member.entity.MemberEntity;
import com.idlelife.myasset.models.member.entity.MemberRoleEntity;
import com.idlelife.myasset.models.member.entity.MemberTokenEntity;
import com.idlelife.myasset.service.AuthService;
import com.idlelife.myasset.service.MemberService;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuthProvider {
    @Autowired
    AuthService authService;

    @Autowired
    MemberService memberService;


    private static final String BEARER_TYPE = "bearer ";
    private static final String AUTHORITIES_KEY = "auth";
    private static final String ACCESS_USER_ID = "memberId";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000L * 60 * 10;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 365;

    @Value("${jwt.secret.signature}")
    private String signatureKey;

    @PostConstruct
    protected void init() {
        signatureKey = Base64.getEncoder().encodeToString(signatureKey.getBytes());
    }

    private final UserDetailsService userDetailsService;

    /**
     * @throws Exception
     * @method 설명 : jwt 토큰 발급
     */
    public String createToken(
            Long id,
            String username,
            List<String> role) {

        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

        final JwtBuilder builder = Jwts.builder()
                .setSubject(username)
                .setExpiration(accessTokenExpiresIn)
                .claim(AUTHORITIES_KEY, role)
                .claim(ACCESS_USER_ID, id)
                .signWith(SignatureAlgorithm.HS256, signatureKey);

        return BEARER_TYPE + builder.compact();
    }

    public String createToken(MemberEntity member){
        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);

        final JwtBuilder builder = Jwts.builder()
                .setSubject(member.getEmail())
                .setExpiration(accessTokenExpiresIn)
                .claim(AUTHORITIES_KEY, "MEMBER")
                .claim(ACCESS_USER_ID, member.getMemberId())
                .signWith(SignatureAlgorithm.HS256, signatureKey);

        return BEARER_TYPE + builder.compact();
    }

    /**
     * @throws Exception
     * @method 설명 : jwt 리프레쉬 토큰 발급
     */
    public String createRefreshToken() {

        long now = (new Date()).getTime();
        Date refreshTokenExpiresIn = new Date(now + REFRESH_TOKEN_EXPIRE_TIME);

        final JwtBuilder builder = Jwts.builder()
                .setExpiration(refreshTokenExpiresIn)
                .signWith(SignatureAlgorithm.HS256, signatureKey);

        return BEARER_TYPE + builder.compact();
    }

    // 토큰에서 회원 정보 추출
    public String getUserPk(String token) {
        return Jwts.parser().setSigningKey(signatureKey).parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * @method 설명 : 컨텍스트에 해당 유저에 대한 권한을 전달하고 API 접근 전 접근 권한을 확인하여 접근 허용 또는 거부를 진행한다.
     */
    @SuppressWarnings("unchecked")
    public Authentication getAuthentication(String token) {
        Claims claims = null;
        // 토큰 기반으로 유저의 정보 파싱
        try{
            claims = Jwts.parser().setSigningKey(signatureKey).parseClaimsJws(token).getBody();
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
        List<MemberRoleEntity> roleList = authService.getMemberRoleList(param);
        for(MemberRoleEntity roleEntity : roleList) {
            roles.add("ROLE_" + roleEntity.getRoleCd());
        }
        CustomUserDetails userDetails = new CustomUserDetails(id, username, roles);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
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
            Jws<Claims> claims = Jwts.parser().setSigningKey(signatureKey).parseClaimsJws(token);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @method 설명 : 리프레쉬 토큰 유효시간 만료여부 검사 실행
     */
    public boolean validateRefreshToken(String refreshToken) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(signatureKey).parseClaimsJws(refreshToken);
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public Jws<Claims> getClaims(String token){
        Jws<Claims> claims = Jwts.parser().setSigningKey(signatureKey).parseClaimsJws(token);
        return claims;
    }

    public String getRefreshTokenByMember(MemberEntity member){
//        Authentication auth = getAuthentication(token);
//        Long memberId = ((CustomUserDetails)auth.getPrincipal()).getId();
//        String email = ((CustomUserDetails)auth.getPrincipal()).getUsername();
        MemberSearch memberSearch = new MemberSearch();
        memberSearch.setEmail(member.getEmail());
        memberSearch.setMemberId(member.getMemberId());
        String refreshToken = "";
        MemberTokenEntity tokenInfo = authService.getMemberToken(memberSearch);
        if(tokenInfo == null){
            refreshToken = createRefreshToken();
            tokenInfo = new MemberTokenEntity();
            tokenInfo.setRefreshToken(refreshToken);
            tokenInfo.setMemberId(member.getMemberId());
            tokenInfo.setDeleteYn("N");
            Jws<Claims> claims = getClaims(refreshToken);
            LocalDateTime refreshTokenExpireDatetime = claims.getBody().getExpiration().toInstant() // Date -> Instant
                    .atZone(ZoneId.systemDefault()) // Instant -> ZonedDateTime
                    .toLocalDateTime();
            tokenInfo.setRefreshTokenExpireDatetime(refreshTokenExpireDatetime);
            authService.regMemberToken(tokenInfo);
            log.info("리프레쉬 토큰 신규 등록 완료;");
        }else{
            refreshToken = tokenInfo.getRefreshToken();
            if(tokenInfo.getRemainingRefreshTokenSeconds() <= (60 * 60 * 24 * 30)){
                refreshToken = createRefreshToken();
                Jws<Claims> claims = getClaims(refreshToken);
                LocalDateTime refreshTokenExpireDatetime = claims.getBody().getExpiration().toInstant() // Date -> Instant
                        .atZone(ZoneId.systemDefault()) // Instant -> ZonedDateTime
                        .toLocalDateTime();
                tokenInfo.setMemberId(member.getMemberId());
                tokenInfo.setDeleteYn("N");
                tokenInfo.setRefreshToken(refreshToken);
                tokenInfo.setRefreshTokenExpireDatetime(refreshTokenExpireDatetime);
                authService.modMemberToken(tokenInfo);
                log.info("리프레쉬 토큰 새로 발급하여 연장 등록 완료");
            }else{
                log.info("리프레쉬 토큰 유지");
            }
        }

        return refreshToken;
    }
}
