package com.idlelife.myasset.models.dto.form;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StockKindForm {
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
}
