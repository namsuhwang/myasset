package com.idlelife.myasset.models.dto.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;


@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AssetBankForm {
    private Long assetId;
    private String acnoType;      // 계좌유형
    private String bankCd;        // 은행코드
    private String bankName;      // 은행명
    private String acno;          // 계좌번호
    private Long ableAmt;         // 출금가능금액
    private Long loanBalAmt;      // 대출잔액
    private Float intRate;        // 예금금리
    private Float loanRate;       // 대출금리
}
