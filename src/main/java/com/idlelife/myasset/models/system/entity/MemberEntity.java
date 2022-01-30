package com.idlelife.myasset.models.system.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class MemberEntity {
    private Long memberId;
    private String memberName;
    private String birthYear;
    private String birthMonth;
    private String birthDay;
    private String sex;
}
