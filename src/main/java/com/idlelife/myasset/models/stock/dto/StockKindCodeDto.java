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
public class StockKindCodeDto {
  private String code;
  private String KindName;
  private String exchange;
}
