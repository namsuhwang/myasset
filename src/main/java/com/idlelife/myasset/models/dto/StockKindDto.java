package com.idlelife.myasset.models.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StockKindDto {
  private Long stockKindId;
  private Long assetId;
  private String stockKindCd;
  private String stockKindName;
  private String stockAcno;
  private Long quantity;
  private Long buyAvgPrice;
  private Long buyTotPrice;
  private Long curPrice;
  private Long curTotPrice;
  private Float pnlRate;
  private Long pnlAmt;
  private String deleteYn;
  private Long memberId;
  private Timestamp regDatetime;
  private Timestamp lastUpdateDatetime;
}
