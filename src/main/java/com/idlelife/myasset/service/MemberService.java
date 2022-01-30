package com.idlelife.myasset.service;

import com.idlelife.myasset.models.member.MemberSearch;
import com.idlelife.myasset.models.member.dto.MemberDto;
import com.idlelife.myasset.models.member.entity.MemberEntity;
import com.idlelife.myasset.models.member.form.MemberForm;
import com.idlelife.myasset.models.member.MemberSearch;
import com.idlelife.myasset.models.member.dto.MemberDto;
import com.idlelife.myasset.repository.MemberMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MemberService {
    @Autowired
    MemberMapper memberMapper;


    public MemberDto getMemberDto(MemberSearch dom){
        return memberMapper.selectMemberDto(dom);
    }

    public MemberEntity getMember(Long memberId){
        return memberMapper.selectMember(memberId);
    }

    public MemberDto regMember(MemberForm form){
        MemberEntity memberEntity = getMemberEntityFromForm(form);
        int cnt = memberMapper.insertMember(memberEntity);
        if(cnt < 1){
            throw new RuntimeException();
        }

        return memberMapper.selectMemberDto(new MemberSearch(memberEntity.getMemberId()));
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
