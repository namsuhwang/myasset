package com.idlelife.myasset.models.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ScrapStockKindDto {
    private String kindCode;             // 종목코드
    private String stockName;            // 종목명
    private String StockType;            // 코스피/코스닥
    private String price;                // 현재가
    private String ydPrice;              // 전일가
    private String diffAmount;           // 전일비(전일대비 차이)
    private String dayRange;             // 등락률(전일대비 등락율)
    private String highPrice;            // 당일 고가
    private String lowPrice;             // 당일 저가
}
