package com.idlelife.myasset.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AssetReDto {
    private Long assetId;
    private String reType;
    private String reTypeName;
    private String reName;
    private String buyDate;
    private Long buyAmt;
    private Long buyExtraAmt;
    private Long loanAmt;
    private Long loanBalAmt;
    private Long loanRate;
    private Long deposit;
    private Long monthFee;
    private Long curExpectAmt;
    private Long curCostAmt;
    private String saleDate;
    private Long saleAmt;
    private Long saleIncomeTaxAmt;
    private Long saleExtraAmt;
    private Long palAmt;
    private Float palPercentage;
    private Long reRentId;
    private String deleteYn;
    private Long memberId;
    private Timestamp regDatetime;
    private Timestamp lastUpdateDatetime;
}