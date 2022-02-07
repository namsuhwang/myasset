package com.idlelife.myasset.service;

import com.idlelife.myasset.models.member.MemberSearch;
import com.idlelife.myasset.models.member.dto.MemberAuthDto;
import com.idlelife.myasset.models.member.dto.MemberDto;
import com.idlelife.myasset.models.member.entity.MemberRoleEntity;
import com.idlelife.myasset.repository.MemberMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional
public class AuthService {

    @Autowired
    MemberMapper memberMapper;


    public MemberAuthDto getMemberAuth(String email){
        MemberAuthDto memberAuthDto = new MemberAuthDto();

        MemberSearch param = new MemberSearch();
        param.setDeleteYn("N");
        param.setEmail(email);
        MemberDto memberDto = memberMapper.selectMemberDto(param);

        List<String> roles = new ArrayList<>();
        MemberSearch memberSearch = new MemberSearch(memberDto.getMemberId());
        memberSearch.setDeleteYn("N");
        List<MemberRoleEntity> memberRoleEntityList = memberMapper.selectMemberRoleList(memberSearch);
        for(MemberRoleEntity roleEntity : memberRoleEntityList){
            roles.add(roleEntity.getRoleCd());
        }

        memberAuthDto.setMemberId(memberDto.getMemberId());
        memberAuthDto.setEmail(memberDto.getEmail());
        memberAuthDto.setRoles(roles);
        log.info("로그인 사용자 정보(memberAuthDto) : " + memberAuthDto);
        return memberAuthDto;
    }




}
