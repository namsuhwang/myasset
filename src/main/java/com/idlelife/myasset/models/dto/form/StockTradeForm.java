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
  private Long stockKindId;
  private String stockKindCd;
  private String exchange;
  private String trType;
  private String trDate;
  private String trTypeName;
  private Long quantity;
  private Long unitPrice;
  private Long trAmt;
  private Long taxAmt;
  private Long feeAmt;
}
