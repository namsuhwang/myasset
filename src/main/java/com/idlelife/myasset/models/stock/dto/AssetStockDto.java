package com.idlelife.myasset.models.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AssetStockDto {
    private long assetId;
    private String orgCd;
    private String orgName;
    private String stockAcno;
    private Long ableAmt;
    private Long loanBalAmt;
    private Float loanRate;
    private String deleteYn;
    private Long memberId;
    private Timestamp regDatetime;
    private Timestamp lastUpdateDatetime;
}
