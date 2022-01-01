package com.idlelife.myasset.models.dto.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AssetReForm {
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
}