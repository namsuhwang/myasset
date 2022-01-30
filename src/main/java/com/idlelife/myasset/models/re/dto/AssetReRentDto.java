package com.idlelife.myasset.models.re.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AssetReRentDto {
    private Long reRentId;
    private Long assetId;
    private String rentStartDate;
    private String rentEndDate;
    private String renterName;
    private String renterContact;
    private Long deposit;
    private Long monthFee;
    private String deleteYn;
    private Timestamp regDatetime;
    private Timestamp lastUpdateDatetime;
}