package com.idlelife.myasset.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TotalAssetSummaryDto {
    private Long totalNetAsset;
    private Long totalAsset;
    private Long TotalLoanBalAmt;
    private Long stockEvalAmt;
    private Long ableAmt;
    private Long reEvalAmt;
}
