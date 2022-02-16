package com.idlelife.myasset.controller;


import com.idlelife.myasset.common.CommonUtil;
import com.idlelife.myasset.common.auth.JwtTokenProvider;
import com.idlelife.myasset.common.exception.MyassetException;
import com.idlelife.myasset.models.common.ErrorCode;
import com.idlelife.myasset.models.member.MemberSearch;
import com.idlelife.myasset.models.member.dto.MemberDto;
import com.idlelife.myasset.models.member.entity.MemberEntity;
import com.idlelife.myasset.models.member.entity.MemberTokenEntity;
import com.idlelife.myasset.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.HashMap;
import java.util.Map;

//@Api(tags = "[ 나의 자산 ]")
@Slf4j
@RestController
@EnableWebMvc
@RequiredArgsConstructor
@RequestMapping(value = "/myasset", produces="application/json;charset=UTF-8")
public class AuthController {
//    @Autowired
//    AuthService authService;

    @Autowired
    MemberService memberService;

    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/auth/loginMember")
    public ResponseEntity<Map<String, Object>> loginMember(
            @RequestBody MemberDto dom
    ){
        log.info("call : /auth/loginMember");
        log.info("params : " + dom.toString());
        Map<String, Object> result = memberService.loginMember(dom);
//        String token = String.valueOf(result.get("token"));
//        MemberAuthDto memberAuthDto = (MemberAuthDto)result.get("memberInfo");

        log.info("loginMember result : " + result.toString());
        return ResponseEntity.ok()
                .body(result);
    }

    @PostMapping("/auth/getToken")
    public ResponseEntity<Map<String, String>> getToken(
            @RequestHeader Map<String, String> headerData
    ){
        log.info("call : /auth/getToken");
        log.info("call : refreshToken = " + headerData.get("refreshtoken"));
        String refreshToken = CommonUtil.parseWebReqParam(headerData.get("refreshtoken"));
        if(CommonUtil.isNullEmpty(refreshToken)){
            log.error("리프레쉬 토큰 미입력");
            throw new MyassetException(ErrorCode.NULL_RF_TOKEN);
        }
        MemberSearch memberSearch = new MemberSearch();
        memberSearch.setRefreshToken(refreshToken);
        MemberTokenEntity refreshTokenInfo = memberService.getMemberToken(memberSearch);
        if(refreshTokenInfo == null){
            log.error("존재하지 않는 리프레쉬 토큰");
            throw new MyassetException(ErrorCode.NOT_EXIST_RF_TOKEN);
        }

        MemberEntity member = memberService.getMember(refreshTokenInfo.getMemberId());
        String newToken = jwtTokenProvider.createAccessToken(member.getEmail());
        String newRefreshToken = jwtTokenProvider.createRefreshToken(member.getEmail());
        MemberTokenEntity tokenEntity = new MemberTokenEntity();
        tokenEntity.setMemberId(member.getMemberId());
        tokenEntity.setRefreshToken(newRefreshToken);
        tokenEntity.setRefreshTokenExpireDatetime(jwtTokenProvider.getTokenExpireDatetime(newRefreshToken));
        tokenEntity.setDeleteYn("N");
        memberService.modMemberToken(tokenEntity);
        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("accesstoken", newToken);
        resultMap.put("refreshtoken", newRefreshToken);

        log.info("getToken result : " + resultMap.toString());

        return ResponseEntity.ok()
                .body(resultMap);
    }

}
