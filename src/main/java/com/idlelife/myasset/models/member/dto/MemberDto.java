package com.idlelife.myasset.models.member.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class MemberDto {
    private Long memberId;
    private String memberName;
    private String email;
    private String pwd;
    private String birth;
    private String gender;
}
