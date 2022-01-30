package com.idlelife.myasset.models.stock.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AssetStockEntity {
    private Long assetId;
    private Long memberId;
    private String orgCd;
    private String orgName;
    private String stockAcno;
    private Long ableAmt;
    private Long loanBalAmt;
    private Float loanRate;
    private String deleteYn;
    private Timestamp regDatetime;
    private Timestamp lastUpdateDatetime;
}
