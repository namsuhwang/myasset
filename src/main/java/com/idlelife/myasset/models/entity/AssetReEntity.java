package com.idlelife.myasset.models.entity;

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
public class AssetReEntity {
    private Long assetId;
    private String reType;
    private String reName;
    private String buyDate;
    private Long buyAmt;
    private Long buyExtraAmt;
    private Long loanAmt;
    private Long loanBalAmt;
    private Float loanRate;
    private String saleDate;
    private Long saleAmt;
    private Long saleIncomeTaxAmt;
    private Long saleExtraAmt;
    private Long palAmt;
    private Long palPercentage;
    private Long reRentId;
    private String deleteYn;
    private Timestamp regDatetime;
    private Timestamp lastUpdateDatetime;
}