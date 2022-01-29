package com.idlelife.myasset.models.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class StockTradeHistoryDto {
    Long realPnlAmt;
    Double realPnlRate;
    List<StockTradeDto> list;
}
