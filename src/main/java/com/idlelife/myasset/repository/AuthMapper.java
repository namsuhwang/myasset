package com.idlelife.myasset.repository;

import com.idlelife.myasset.models.member.MemberSearch;
import com.idlelife.myasset.models.member.dto.MemberDto;
import com.idlelife.myasset.models.member.entity.MemberEntity;
import com.idlelife.myasset.models.member.entity.MemberRoleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AuthMapper {

    int insertMemberRole(MemberRoleEntity dom);

    int deleteMemberRole(MemberRoleEntity dom);

    List<MemberRoleEntity> selectMemberRoleList(MemberSearch dom);

    MemberRoleEntity selectMemberRole(MemberSearch dom);
}
