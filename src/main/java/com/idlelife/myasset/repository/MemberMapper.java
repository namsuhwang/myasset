package com.idlelife.myasset.repository;

import com.idlelife.myasset.models.member.MemberSearch;
import com.idlelife.myasset.models.member.dto.MemberDto;
import com.idlelife.myasset.models.member.entity.MemberEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MemberMapper {

    long createMemberId();

    int insertMember(MemberEntity dom);

    int updateMember(MemberEntity dom);

    int deleteMember(long memberId);

    List<MemberDto> selectMemberDtoList(MemberSearch dom);

    MemberEntity selectMember(long memberId);

    MemberDto selectMemberDto(MemberSearch dom);

}
