package com.idlelife.myasset.common.auth;

import com.idlelife.myasset.common.exception.MyassetException;
import com.idlelife.myasset.models.member.MemberSearch;
import com.idlelife.myasset.models.member.dto.MemberDto;
import com.idlelife.myasset.repository.MemberMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.idlelife.myasset.models.common.ErrorCode.MYASSET_ERROR_1006;

@RequiredArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    MemberMapper memberMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        MemberSearch sqlParam = new MemberSearch();
        sqlParam.setEmail(email);
        MemberDto memberDto = memberMapper.selectMemberDto(sqlParam);
        if(memberDto == null){
            throw new MyassetException(MYASSET_ERROR_1006);
        }

        List<String> roles = new ArrayList<>();
        roles.add("MEMBER");
        UserDetails member = new CustomUserDetails(memberDto.getMemberId(), email, roles);

        return member;
    }

}
