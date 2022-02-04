package com.idlelife.myasset.controller;


import com.idlelife.myasset.common.auth.AuthProvider;
import com.idlelife.myasset.models.member.MemberSearch;
import com.idlelife.myasset.models.member.dto.MemberAuthDto;
import com.idlelife.myasset.models.member.dto.MemberDto;
import com.idlelife.myasset.models.member.form.MemberForm;
import com.idlelife.myasset.service.AuthService;
import com.idlelife.myasset.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;
import java.util.Map;

//@Api(tags = "[ 나의 자산 ]")
@Slf4j
@RestController
@EnableWebMvc
@RequiredArgsConstructor
@RequestMapping(value = "/myasset", produces="application/json;charset=UTF-8")
public class AuthController {
    @Autowired
    AuthService authService;

    @Autowired
    MemberService memberService;

    private final AuthProvider authProvider;


    @PostMapping("/auth/loginMember")
    public ResponseEntity<MemberAuthDto> loginMember(
            @RequestBody MemberDto dom
    ){
        log.info("call : /auth/loginMember");
        log.info("params : " + dom.toString());
        Map<String, Object> result = authService.loginMember(dom);
        String token = String.valueOf(result.get("token"));
        MemberAuthDto memberAuthDto = (MemberAuthDto)result.get("memberInfo");

        return ResponseEntity.ok()
                .header("accesstoken", token)
                .body(memberAuthDto);
    }



}
