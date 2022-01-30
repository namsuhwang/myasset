package com.idlelife.myasset.models.member.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberEntity {
    private Long memberId;
    private String memberName;
    private String email;
    private String pwd;
    private String birthYear;
    private String birthMonth;
    private String birthDay;
    private String gender;
    private String deleteYn;
    private Timestamp regDatetime;
    private Timestamp lastUpdateDatetime;
}
