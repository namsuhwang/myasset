package com.idlelife.myasset.models.stock;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StockSearch {
    private Long assetId;
    private String orgCd;
    private String stockAcno;
    private Long memberId;
    private String stockKindCd;
    private String stockKindName;
    private String stockTradeType;
    private String searchStartDate;
    private String searchEndDate;
    private String searchWord;
    private String deleteYn;
    private Long stockTradeId;
    private Long stockKindId;
    private Long orderNo;
    private Long startOrderNo;
    private Long endOrderNo;
}
