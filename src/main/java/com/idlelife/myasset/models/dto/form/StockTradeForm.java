package com.idlelife.myasset.models.dto.form;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StockTradeForm {
  private Long stockTradeId;
  private String trType;
  private String trDate;
  private String trTypeName;
  private Long stockKindId;
  private Long quantity;
  private Long unitPrice;
  private Long trAmt;
  private Long taxAmt;
  private Long feeAmt;
  private Long befQuantity;
  private Long aftQuantity;
  private Long befBuyAvgPrice;
  private Long befBuyTotPrice;
  private Long aftBuyAvgPrice;
  private Long aftBuyTotPrice;
  private Double pnlRate;
  private Long pnlAmt;
}
