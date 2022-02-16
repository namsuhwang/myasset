package com.idlelife.myasset.service;

import com.idlelife.myasset.common.auth.JwtTokenProvider;
import com.idlelife.myasset.common.exception.MyassetException;
import com.idlelife.myasset.models.member.MemberSearch;
import com.idlelife.myasset.models.member.dto.MemberAuthDto;
import com.idlelife.myasset.models.member.dto.MemberDto;
import com.idlelife.myasset.models.member.entity.MemberEntity;
import com.idlelife.myasset.models.member.entity.MemberRoleEntity;
import com.idlelife.myasset.models.member.entity.MemberTokenEntity;
import com.idlelife.myasset.models.member.form.MemberForm;
import com.idlelife.myasset.repository.MemberMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
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

    @Autowired
    AuthService authService;


    private final JwtTokenProvider jwtTokenProvider;

    private final BCryptPasswordEncoder passwordEncoder;


    public Map<String, Object> loginMember(MemberDto dom){
        // 비밀번호 체크
        if(!passwordEncoder.matches(dom.getPwd(), getPwd(dom.getEmail()))){
            throw new MyassetException(UNMATCHED_AUTH_INFO_EXCEPTION);
        }

        // 엑세스 토큰 발급. 로그인할 때마다 발급함.
        String accesstoken = jwtTokenProvider.createAccessToken(dom.getEmail());
        log.info("loginMember accesstoken=" + accesstoken);

        // 회원정보 조회
        MemberAuthDto memberAuthDto = authService.getMemberAuth(dom.getEmail());

        String refreshToken = jwtTokenProvider.createRefreshToken(memberAuthDto.getEmail());
        MemberTokenEntity tokenEntity = new MemberTokenEntity();
        tokenEntity.setMemberId(memberAuthDto.getMemberId());
        tokenEntity.setRefreshToken(refreshToken);
        tokenEntity.setDeleteYn("N");
        tokenEntity.setRefreshTokenExpireDatetime(jwtTokenProvider.getTokenExpireDatetime(refreshToken));
        modMemberToken(tokenEntity);
        log.info("리프레쉬 토큰 업데이트 완료 : " + refreshToken);
        Map<String, Object> result = new HashMap<>();
        result.put("memberInfo", memberAuthDto);
        result.put("accesstoken", accesstoken);
        result.put("refreshtoken", refreshToken);

        return result;
    }

    public Map<String, Object> regMember(MemberForm form){
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
            throw new MyassetException("DB 에러 : Member Insert 에러", MYASSET_ERROR_1000);
        }
        memberDto = memberMapper.selectMemberDto(param);
        MemberRoleEntity memberRoleEntity = new MemberRoleEntity();
        memberRoleEntity.setMemberId(memberEntity.getMemberId());
        memberRoleEntity.setRoleCd("MEMBER");
        memberRoleEntity.setDeleteYn("N");
        regMemberRole(memberRoleEntity);

        MemberAuthDto memberInfo = new MemberAuthDto();
        memberInfo.setMemberId(memberDto.getMemberId());
        memberInfo.setEmail(memberDto.getEmail());
        memberInfo.setRoles(getMemberRoles(memberDto.getMemberId()));

        // 엑세스토큰과 리프레쉬 토큰을 생성하여 반환
        List<String> role = getMemberRoles(memberDto.getMemberId());
        String accessToken = jwtTokenProvider.createAccessToken(memberDto.getEmail());

        String refreshToken = jwtTokenProvider.createRefreshToken(memberEntity.getEmail());
        MemberTokenEntity tokenEntity = new MemberTokenEntity();
        tokenEntity.setMemberId(memberDto.getMemberId());
        tokenEntity.setRefreshToken(refreshToken);
        tokenEntity.setDeleteYn("N");
        tokenEntity.setRefreshTokenExpireDatetime(jwtTokenProvider.getTokenExpireDatetime(refreshToken));
        regMemberToken(tokenEntity);
        log.info("리프레쉬 토큰 신규 등록 완료;");
        Map<String, Object> result = new HashMap<>();
        result.put("memberInfo", memberInfo);
        result.put("accesstoken", accessToken);
        result.put("refreshtoken", refreshToken);
        log.info("regMember 결과 =" + result.toString());
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

    public void regMemberRole(MemberRoleEntity dom ){
        int cnt = memberMapper.insertMemberRole(dom);
        if(cnt < 1){
            throw new MyassetException("DB 에러 : MemberRole Insert 에러", MYASSET_ERROR_1000);
        }
        return;
    }

    public List<MemberDto> getMemberDtoList(MemberSearch dom){
        List<MemberDto> list = memberMapper.selectMemberDtoList(dom);
        return list;
    }

    public List<String> getMemberRoles(Long memberId){
        List<String> roleList = new ArrayList<>();
        MemberSearch memberSearch = new MemberSearch(memberId);
        memberSearch.setDeleteYn("N");
        List<MemberRoleEntity> memberRoleEntityList = getMemberRoleList(memberSearch);
        for(MemberRoleEntity roleEntity : memberRoleEntityList){
            roleList.add(roleEntity.getRoleCd());
        }
        return roleList;
    }

    private List<MemberRoleEntity> getMemberRoleList(MemberSearch dom){
        List<MemberRoleEntity>  roleList = memberMapper.selectMemberRoleList(dom);
        return roleList;
    }

    private String getPwd(String email){
        MemberSearch param = new MemberSearch();
        param.setDeleteYn("N");
        param.setEmail(email);
        MemberDto memberDto = getMemberDto(param);
        return memberDto.getPwd();
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



    public void regMemberToken(MemberTokenEntity dom ){
        memberMapper.insertMemberToken(dom);
        return;
    }

    public void modMemberToken(MemberTokenEntity dom ){
        memberMapper.updateMemberToken(dom);
        return;
    }

    public MemberTokenEntity getMemberToken(MemberSearch dom){
        MemberTokenEntity token = memberMapper.selectMemberToken(dom);
        return token;
    }
}
