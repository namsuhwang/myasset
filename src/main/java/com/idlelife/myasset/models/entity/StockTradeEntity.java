package com.idlelife.myasset.models.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StockTradeEntity {
  private Long stockTradeId;
  private String tradeType;
  private Timestamp tradeDatetime;
  private String tradeTypeName;
  private Long stockKindId;
  private Long tradeQuantity;
  private Long tradeUnitPrice;
  private Long tradeAmt;
  private Long befQuantity;
  private Long aftQuantity;
  private Long befBuyAvgPrice;
  private Long befBuyTotPrice;
  private Long aftBuyAvgPrice;
  private Long aftBuyTotPrice;
  private double pnlRate;
  private Long pnlAmt;
  private String deleteYn;
  private Timestamp regDatetime;
  private Timestamp lastUpdateDatetime;
}
