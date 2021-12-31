package com.idlelife.myasset.models.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AssetEntity {
    private Long assetId;
    private Long memberId;
    private String assetType;
    private String assetName;
    private Long evalAmt;
    private String deleteYn;
    private LocalDateTime regDatetime;
    private LocalDateTime lastUpdateDatetime;
}