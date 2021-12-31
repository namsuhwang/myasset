package com.idlelife.myasset.models.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AssetDto {
    private Long assetId;
    private Long memberId;
    private String AssetType;
    private String AssetTypeName;
    private String AssetName;
    private Long evalAmt;
    private String deleteYn;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime regDatetime;

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime lastUpdateDatetime;
}
