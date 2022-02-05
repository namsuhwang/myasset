package com.idlelife.myasset.service;

import com.idlelife.myasset.common.CommonUtil;
import com.idlelife.myasset.common.auth.AuthProvider;
import com.idlelife.myasset.common.auth.RsaUtil;
import com.idlelife.myasset.common.exception.MyassetException;
import com.idlelife.myasset.models.member.MemberSearch;
import com.idlelife.myasset.models.member.dto.MemberAuthDto;
import com.idlelife.myasset.models.member.dto.MemberDto;
import com.idlelife.myasset.models.member.entity.MemberRoleEntity;
import com.idlelife.myasset.repository.AuthMapper;
import com.idlelife.myasset.repository.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.PrivateKey;
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
        String token = authProvider.createToken(memberDto.getMemberId(), memberDto.getEmail(), role);
        log.info("loginMember token=" + token);

        MemberAuthDto memberAuthDto = new MemberAuthDto();
        memberAuthDto.setMemberId(memberDto.getMemberId());
        // memberAuthDto.setToken(token);
        memberAuthDto.setEmail(memberDto.getEmail());
        memberAuthDto.setRole(role);

        Map<String, Object> result = new HashMap<>();
        result.put("memberInfo", memberAuthDto);
        result.put("token", token);

        return result;
    }

    public List<MemberRoleEntity> getMemberRoleList(MemberSearch dom){
        List<MemberRoleEntity>  roleList = authMapper.selectMemberRoleList(dom);
        return roleList;
    }
}
