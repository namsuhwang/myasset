package com.idlelife.myasset.models.stock.dto;


import com.idlelife.myasset.models.stock.dto.StockKindDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TotalStockAssetDto {
  private String baseTime;
  private Long totBuyPrice;     // 총매입금액
  private Long totCurPrice;     // 총평가금액
  private Double totPnlRate;    // 총수익률
  private Long totPnlAmt;       // 총손익금액

  List<StockKindTotalDto> list;
}
