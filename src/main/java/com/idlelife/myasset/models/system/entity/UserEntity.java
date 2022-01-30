package com.idlelife.myasset.models.system.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@AllArgsConstructor
public class UserEntity {
    private String userId;
    private String userName;
    private String userPwd;
    private Long memberId;
}
