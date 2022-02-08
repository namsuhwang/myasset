package com.idlelife.myasset.models.stock.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StockInterestEntity {
  private Long stockInterestId;
  private String stockKindCd;
  private String stockKindName;
  private Long orderNo;
  private Long memberId;
  private Timestamp regDatetime;
  private Timestamp lastUpdateDatetime;
}
