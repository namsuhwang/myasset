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
public class StockKindEntity {
  private Long stockKindId;
  private Long assetId;
  private Long memberId;
  private String stockKindCd;
  private String stockKindName;
  private Long quantity;
  private Long buyAvgPrice;
  private Long buyTotPrice;
  private Long curUnitPrice;
  private Long curTotPrice;
  private Double pnlRate;
  private Long pnlAmt;
  private String deleteYn;
  private Timestamp regDatetime;
  private Timestamp lastUpdateDatetime;
}
