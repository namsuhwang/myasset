package com.idlelife.myasset.models.re.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AssetReRentEntity {
    private Long reRentId;
    private Long assetId;
    private String rentStartDate;
    private String rentEndDate;
    private String renterName;
    private String renterContact;
    private Long deposit;
    private Long monthFee;
    private String deleteYn;
    private String regDatetime;
    private String lastUpdateDatetime;
}