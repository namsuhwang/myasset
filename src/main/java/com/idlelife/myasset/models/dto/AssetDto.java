package com.idlelife.myasset.models.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AssetDto {
    private Long assetId;
    private Long memberId;
    private String AssetType;
    private String AssetTypeName;
    private String AssetName;
    private Long evalAmt;
    private Long ableAmt;
    private String deleteYn;
    private Timestamp regDatetime;
    private Timestamp lastUpdateDatetime;
}
