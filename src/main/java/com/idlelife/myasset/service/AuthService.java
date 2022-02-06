package com.idlelife.myasset.service;

import com.idlelife.myasset.common.CommonUtil;
import com.idlelife.myasset.common.auth.AuthProvider;
import com.idlelife.myasset.common.auth.CustomUserDetails;
import com.idlelife.myasset.common.auth.RsaUtil;
import com.idlelife.myasset.common.exception.MyassetException;
import com.idlelife.myasset.models.common.ErrorCode;
import com.idlelife.myasset.models.member.MemberSearch;
import com.idlelife.myasset.models.member.dto.MemberAuthDto;
import com.idlelife.myasset.models.member.dto.MemberDto;
import com.idlelife.myasset.models.member.entity.MemberEntity;
import com.idlelife.myasset.models.member.entity.MemberRoleEntity;
import com.idlelife.myasset.models.member.entity.MemberTokenEntity;
import com.idlelife.myasset.repository.AuthMapper;
import com.idlelife.myasset.repository.MemberMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.idlelife.myasset.common.auth.RsaUtil.decodePrivateKey;
import static com.idlelife.myasset.models.common.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class AuthService {
    @Autowired
    AuthMapper authMapper;

    @Value("${jwt.secret.signature}")
    private String signatureKey;

    private static final String BEARER_TYPE = "bearer ";
    private static final String AUTHORITIES_KEY = "auth";
    private static final String ACCESS_USER_ID = "memberId";

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
//    public Map<String, Object> loginMember(MemberDto dom){
//        MemberSearch param = new MemberSearch();
//        param.setDeleteYn("N");
//        param.setEmail(dom.getEmail());
//        MemberDto memberDto = memberService.getMemberDto(param);
//
//        if(!passwordEncoder.matches(dom.getPwd(), memberDto.getPwd())){
//            throw new MyassetException(UNMATCHED_AUTH_INFO_EXCEPTION);
//        }
//
//        List<String> role = memberService.getMemberRole(memberDto.getMemberId());
//        String accesstoken = authProvider.createToken(memberDto.getMemberId(), memberDto.getEmail(), role);
//        log.info("loginMember accesstoken=" + accesstoken);
//
//        MemberAuthDto memberAuthDto = new MemberAuthDto();
//        memberAuthDto.setMemberId(memberDto.getMemberId());
//        // memberAuthDto.setToken(token);
//        memberAuthDto.setEmail(memberDto.getEmail());
//        memberAuthDto.setRole(role);
//
//        Map<String, Object> result = new HashMap<>();
//        result.put("memberInfo", memberAuthDto);
//        result.put("accesstoken", accesstoken);
//        /*
//        String refreshToken = "";
//        MemberTokenEntity refreshTokenInfo = getMemberToken(param);
//        if(refreshTokenInfo == null){
//            Jws<Claims> claims = authProvider.getClaims(refreshToken);
//        }else{
//            refreshToken = refreshTokenInfo.getRefreshToken();
//        }
//        result.put("refreshtoken", refreshToken);
//         */
//
//        return result;
//    }
//
//
//    public void regMemberRole(MemberRoleEntity dom ){
//        int cnt = authMapper.insertMemberRole(dom);
//        if(cnt < 1){
//            throw new MyassetException("DB 에러 : MemberRole Insert 에러", MYASSET_ERROR_1000);
//        }
//        return;
//    }
//
//    public List<MemberRoleEntity> getMemberRoleList(MemberSearch dom){
//        List<MemberRoleEntity>  roleList = authMapper.selectMemberRoleList(dom);
//        return roleList;
//    }
//
//    public void regMemberToken(MemberTokenEntity dom ){
//        authMapper.insertMemberToken(dom);
//        return;
//    }
//
//    public void modMemberToken(MemberTokenEntity dom ){
//        authMapper.updateMemberToken(dom);
//        return;
//    }
//
//    public MemberTokenEntity getMemberToken(MemberSearch dom){
//        MemberTokenEntity token = authMapper.selectMemberToken(dom);
//        return token;
//    }
//
//    public String createToken(Long memberId, String email, List<String> role){
//        String token = authProvider.createToken(memberId, email, role);
//        return token;
//    }
//
//    public String createRefreshToken(Long memberId){
//        String refreshToken = authProvider.createRefreshToken();
//        MemberTokenEntity tokenEntity = new MemberTokenEntity();
//        tokenEntity.setMemberId(memberId);
//        tokenEntity.setRefreshToken(refreshToken);
//        tokenEntity.setDeleteYn("N");
//        LocalDateTime expireDatetime = LocalDateTime.now().plusDays(365);
//        tokenEntity.setRefreshTokenExpireDatetime(expireDatetime);
//        regMemberToken(tokenEntity);
//        return refreshToken;
//    }

}
