package com.idlelife.myasset.models.member.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberTokenEntity {
    private Long memberId;
    private String refreshToken;
    private LocalDateTime refreshTokenExpireDatetime;
    private Long remainingRefreshTokenSeconds;
    private String deleteYn;
    private LocalDateTime regDatetime;
    private LocalDateTime lastUpdateDatetime;
}
