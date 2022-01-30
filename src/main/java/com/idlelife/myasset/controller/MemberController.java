package com.idlelife.myasset.controller;


import com.idlelife.myasset.models.member.MemberSearch;
import com.idlelife.myasset.models.member.dto.MemberDto;
import com.idlelife.myasset.models.member.form.MemberForm;
import com.idlelife.myasset.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

//@Api(tags = "[ 나의 자산 ]")
@Slf4j
@RestController
@EnableWebMvc
@RequestMapping(value = "/myasset", produces="application/json;charset=UTF-8")
public class MemberController {
    @Autowired
    MemberService memberService;

    @PostMapping("/member/reg")
    public ResponseEntity<MemberDto> regMember(
            @RequestBody MemberForm dom
    ){
        log.info("call : /member/reg");
        log.info("params : " + dom.toString());
        MemberDto result = memberService.regMember(dom);

        return ResponseEntity.ok().body(result);
    }


    @PostMapping("/member/mod")
    public ResponseEntity<MemberDto> modMember(
            @RequestBody MemberForm dom
    ){
        log.info("call : /member/mod");
        log.info("params : " + dom.toString());
        MemberDto result = memberService.modMember(dom);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/member/del")
    public ResponseEntity<MemberDto> delMember(
            @RequestBody MemberForm dom
    ){
        log.info("call : /member/del");
        log.info("params : " + dom.getMemberId());
        MemberDto result = memberService.delMember(dom.getMemberId());

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/member/list")
    public ResponseEntity<List<MemberDto>> getMemberList(
            @RequestBody MemberSearch dom
    ){
        log.info("call : /member/list");
        log.info("params : " + dom.toString());
        List<MemberDto> result = memberService.getMemberDtoList(dom);
        log.info("result : " + result.toString());

        return ResponseEntity.ok().body(result);
    }


}
