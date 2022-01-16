package com.idlelife.myasset.models.dto.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AssetForm {
    private Long assetId;
    private Long memberId;
    private String AssetType;
    private String AssetName;
}
