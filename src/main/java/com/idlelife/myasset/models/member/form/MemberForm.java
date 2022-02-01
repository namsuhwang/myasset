package com.idlelife.myasset.models.member.form;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberForm {
    private Long memberId;
    private String memberName;
    private String email;
    private String pwd;
    private String birth;
    private String gender;
    private String deleteYn;
}
