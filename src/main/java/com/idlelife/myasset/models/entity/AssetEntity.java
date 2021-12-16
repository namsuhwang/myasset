package com.idlelife.myasset.models.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@AllArgsConstructor
public class AssetEntity {
    private Long assetId;
    private Long memberId;
    private String AssetType;
    private String AssetName;
    private Long evalAmt;
    private LocalDateTime lastUpdateDatetime;
}