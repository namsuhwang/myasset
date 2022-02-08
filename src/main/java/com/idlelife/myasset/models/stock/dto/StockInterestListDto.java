package com.idlelife.myasset.models.stock.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StockInterestListDto {
  private String baseTime;

  List<StockInterestDto> list;
}
