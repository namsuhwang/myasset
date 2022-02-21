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
public class StockKindTotalDto {
  private String stockKindCd;
  private String stockKindName;
  private Long quantity;
  private Long buyAvgPrice;
  private Long buyTotPrice;
  private Long curUnitPrice;
  private Long curTotPrice;
  private String diffAmount;           // 전일비(전일대비 차이)
  private String dayRange;             // 등락률(전일대비 등락율)
  private String highPrice;            // 당일 고가
  private String lowPrice;             // 당일 저가
  private Double pnlRate;
  private Long pnlAmt;
  private String baseTime;
}
