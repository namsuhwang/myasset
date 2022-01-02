package com.idlelife.myasset.models.dto.form;

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
public class AssetStockForm {
    private long assetId;
    private String securityCd;
    private String securityName;
    private String stockAcno;
    private Long ableAmt;
    private Long loanBalAmt;
    private Float loanRate;
}
