package com.idlelife.myasset.models.stock.form;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StockInterestForm {
  private String reqDiv;  // 요청구분 (REG:등록, DEL:삭제, ORD:순서조정)
  private Long stockInterestId;
  private String stockKindCd;
  private String stockKindName;
  private Long orderNo;
  private Long memberId;
}
