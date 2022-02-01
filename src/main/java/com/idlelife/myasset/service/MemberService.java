package com.idlelife.myasset.service;

import com.idlelife.myasset.common.auth.AuthProvider;
import com.idlelife.myasset.common.exception.MyassetException;
import com.idlelife.myasset.models.member.MemberSearch;
import com.idlelife.myasset.models.member.dto.MemberAuthDto;
import com.idlelife.myasset.models.member.dto.MemberDto;
import com.idlelife.myasset.models.member.entity.MemberEntity;
import com.idlelife.myasset.models.member.form.MemberForm;
import com.idlelife.myasset.repository.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.idlelife.myasset.models.common.ErrorCode.*;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class MemberService {
    @Autowired
    MemberMapper memberMapper;

    private final BCryptPasswordEncoder passwordEncoder;

    private final AuthProvider authProvider;

    public Map<String, Object> loginMember(MemberDto dom){
        MemberSearch param = new MemberSearch();
        param.setEmail(dom.getEmail());
        MemberDto memberDto = getMemberDto(param);

        if(!passwordEncoder.matches(dom.getPwd(), memberDto.getPwd())){
            throw new MyassetException(UNMATCHED_AUTH_INFO_EXCEPTION);
        }

        String token = authProvider.createToken(memberDto.getMemberId(), memberDto.getEmail(), "MEMBER");
        log.info("loginMember token=" + token);

        MemberAuthDto memberAuthDto = new MemberAuthDto();
        memberAuthDto.setMemberId(memberDto.getMemberId());
        // memberAuthDto.setToken(token);
        memberAuthDto.setEmail(memberDto.getEmail());
        memberAuthDto.setRole("MEMBER");

        Map<String, Object> result = new HashMap<>();
        result.put("memberInfo", memberAuthDto);
        result.put("token", token);

        return result;
    }

    public MemberDto getMemberDto(MemberSearch dom){
        MemberDto memberDto = memberMapper.selectMemberDto(dom);
        if(memberDto == null){
            throw new MyassetException(MYASSET_ERROR_1006);
        }
        return memberDto;
    }

    public MemberEntity getMember(Long memberId){
        return memberMapper.selectMember(memberId);
    }

    public MemberDto regMember(MemberForm form){
        MemberSearch param = new MemberSearch();
        param.setEmail(form.getEmail());
        MemberDto memberDto = memberMapper.selectMemberDto(param);
        if(memberDto != null){
            throw new MyassetException(MYASSET_ERROR_1007);
        }

        MemberEntity memberEntity = getMemberEntityFromForm(form);
        memberEntity.setPwd(passwordEncoder.encode(form.getPwd()));
        log.info("memberEntity : " + memberEntity.toString());
        int cnt = memberMapper.insertMember(memberEntity);
        if(cnt < 1){
            throw new RuntimeException();
        }
        MemberDto result = getMemberDto(param);
        return result;
    }

    public MemberDto modMember(MemberForm form){
        MemberEntity memberEntity = getMemberEntityFromForm(form);
        int cnt = memberMapper.updateMember(memberEntity);
        if(cnt < 1){
            throw new RuntimeException();
        }
        MemberSearch sqlParam = new MemberSearch(memberEntity.getMemberId());
        return memberMapper.selectMemberDto(sqlParam);
    }

    public MemberDto delMember(Long memberId){
        int cnt = memberMapper.deleteMember(memberId);
        if(cnt < 1){
            throw new RuntimeException();
        }
        return memberMapper.selectMemberDto(new MemberSearch(memberId));
    }

    public List<MemberDto> getMemberDtoList(MemberSearch dom){
        List<MemberDto> list = memberMapper.selectMemberDtoList(dom);
        return list;
    }

    public MemberEntity getMemberEntityFromForm(MemberForm form){
        MemberEntity memberEntity = new MemberEntity();
        if(form.getMemberId() == null || form.getMemberId() <= 0){
            long memberId = memberMapper.createMemberId();
            memberEntity.setMemberId(memberId);
        }else{
            memberEntity.setMemberId(form.getMemberId());
        }
        memberEntity.setMemberName(form.getMemberName());
        String[] birth = form.getBirth().split("-");
        memberEntity.setBirthYear(birth[0]);
        memberEntity.setBirthMonth(birth[1]);
        memberEntity.setBirthDay(birth[2]);
        memberEntity.setEmail(form.getEmail());
        memberEntity.setPwd(form.getPwd());
        memberEntity.setGender(form.getGender());
        memberEntity.setDeleteYn("N");
        return memberEntity;
    }

}
