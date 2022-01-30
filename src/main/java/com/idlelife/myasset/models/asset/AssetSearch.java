package com.idlelife.myasset.models.asset;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AssetSearch {
    private Long assetId;
    private Long memberId;
    private String assetType;
    private String searchStartDate;
    private String searchEndDate;
    private String deleteYn;
}
