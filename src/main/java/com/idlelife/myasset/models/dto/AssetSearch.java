package com.idlelife.myasset.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AssetSearch {
    private Long assetId;
    private Long memberId;
    private String assetType;
    private String bankAcnoType;
    private String stockKindCd;
    private String stockTradeType;
    private String searchStartDate;
    private String searchEndDate;
    private String reType;
    private String deleteYn;
    private Long stockTradeId;
    private Long stockKindId;
}
