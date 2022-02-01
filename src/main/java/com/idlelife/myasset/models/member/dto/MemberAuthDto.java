package com.idlelife.myasset.models.member.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberAuthDto {
    private Long memberId;
    private String email;
    private String role;
}
