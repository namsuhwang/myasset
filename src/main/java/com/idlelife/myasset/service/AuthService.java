package com.idlelife.myasset.service;

import com.idlelife.myasset.common.CommonUtil;
import com.idlelife.myasset.common.auth.AuthProvider;
import com.idlelife.myasset.common.auth.RsaUtil;
import com.idlelife.myasset.common.exception.MyassetException;
import com.idlelife.myasset.models.member.MemberSearch;
import com.idlelife.myasset.models.member.dto.MemberAuthDto;
import com.idlelife.myasset.models.member.dto.MemberDto;
import com.idlelife.myasset.models.member.entity.MemberEntity;
import com.idlelife.myasset.models.member.entity.MemberRoleEntity;
import com.idlelife.myasset.models.member.entity.MemberTokenEntity;
import com.idlelife.myasset.repository.AuthMapper;
import com.idlelife.myasset.repository.MemberMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.idlelife.myasset.common.auth.RsaUtil.decodePrivateKey;
import static com.idlelife.myasset.models.common.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class AuthService {
    @Autowired
    MemberService memberService;

    @Autowired
    MemberMapper memberMapper;

    @Autowired
    AuthMapper authMapper;

    private final BCryptPasswordEncoder passwordEncoder;

    private final AuthProvider authProvider;

    public Map<String, Object> loginMember(MemberDto dom){
        MemberSearch param = new MemberSearch();
        param.setDeleteYn("N");
        param.setEmail(dom.getEmail());
        MemberDto memberDto = memberService.getMemberDto(param);

        if(!passwordEncoder.matches(dom.getPwd(), memberDto.getPwd())){
            throw new MyassetException(UNMATCHED_AUTH_INFO_EXCEPTION);
        }

        List<String> role = memberService.getMemberRole(memberDto.getMemberId());
        String accesstoken = authProvider.createToken(memberDto.getMemberId(), memberDto.getEmail(), role);
        log.info("loginMember accesstoken=" + accesstoken);

        MemberAuthDto memberAuthDto = new MemberAuthDto();
        memberAuthDto.setMemberId(memberDto.getMemberId());
        // memberAuthDto.setToken(token);
        memberAuthDto.setEmail(memberDto.getEmail());
        memberAuthDto.setRole(role);

        Map<String, Object> result = new HashMap<>();
        result.put("memberInfo", memberAuthDto);
        result.put("accesstoken", accesstoken);
        /*
        String refreshToken = "";
        MemberTokenEntity refreshTokenInfo = getMemberToken(param);
        if(refreshTokenInfo == null){
            Jws<Claims> claims = authProvider.getClaims(refreshToken);
        }else{
            refreshToken = refreshTokenInfo.getRefreshToken();
        }
        result.put("refreshtoken", refreshToken);
         */

        return result;
    }


    public void regMemberRole(MemberRoleEntity dom ){
        int cnt = authMapper.insertMemberRole(dom);
        if(cnt < 1){
            throw new MyassetException("DB 에러 : MemberRole Insert 에러", MYASSET_ERROR_1000);
        }
        return;
    }

    public List<MemberRoleEntity> getMemberRoleList(MemberSearch dom){
        List<MemberRoleEntity>  roleList = authMapper.selectMemberRoleList(dom);
        return roleList;
    }

    public void regMemberToken(MemberTokenEntity dom ){
        authMapper.insertMemberToken(dom);
        return;
    }

    public void modMemberToken(MemberTokenEntity dom ){
        authMapper.updateMemberToken(dom);
        return;
    }

    public MemberTokenEntity getMemberToken(MemberSearch dom){
        MemberTokenEntity token = authMapper.selectMemberToken(dom);
        return token;
    }

    public String createToken(Long memberId, String email, List<String> role){
        String token = authProvider.createToken(memberId, email, role);
        return token;
    }

    public String createRefreshToken(Long memberId){
        String refreshToken = authProvider.createRefreshToken();
        MemberTokenEntity tokenEntity = new MemberTokenEntity();
        tokenEntity.setMemberId(memberId);
        tokenEntity.setRefreshToken(refreshToken);
        tokenEntity.setDeleteYn("N");
        LocalDateTime expireDatetime = LocalDateTime.now().plusDays(365);
        tokenEntity.setRefreshTokenExpireDatetime(expireDatetime);
        regMemberToken(tokenEntity);
        return refreshToken;
    }

}
