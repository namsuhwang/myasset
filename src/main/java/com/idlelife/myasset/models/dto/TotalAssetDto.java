package com.idlelife.myasset.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TotalAssetDto {
    private Long totalAssetAmt;        // 총 자산금액
    private Long totalNetAssetAmt;     // 총 순자산금액
    private Long totalLoanBalAmt;      // 총 대출잔액

    private Long bankAbleAmt;
    List<AssetBankDto> bankList;

    private Long stockEvalAmt;
    List<StockKindDto> stockList;

    private Long reEvalAmt;
    List<AssetReDto> reList;
}
