package com.idlelife.myasset.models.stock.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StockTradeDto {
  private Long stockTradeId;
  private String tradeType;
  private String tradeDateTime;
  private String tradeTypeName;
  private Long stockKindId;
  private String stockKindCd;
  private String stockKindName;
  private Long tradeQuantity;
  private Long tradeUnitPrice;
  private Long tradeAmt;
  private Long befQuantity;
  private Long aftQuantity;
  private Long befBuyAvgPrice;
  private Long befBuyTotPrice;
  private Long aftBuyAvgPrice;
  private Long aftBuyTotPrice;
  private Long taxAmt;
  private Long feeAmt;
  private double pnlRate;
  private Long pnlAmt;
  private String deleteYn;
  private Timestamp regDatetime;
  private Timestamp lastUpdateDatetime;
}
