package com.idlelife.myasset.models.re.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AssetReRentForm {
    private Long reRentId;
    private Long assetId;
    private String rentStartDate;
    private String rentEndDate;
    private String renterName;
    private String renterContact;
    private Long deposit;
    private Long monthFee;
}