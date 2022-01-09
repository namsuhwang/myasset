package com.idlelife.myasset.models.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    NOT_FOUND(404,"COMMON-ERR-404","PAGE NOT FOUND"),
    INTER_SERVER_ERROR(500,"COMMON-ERR-500","INTER SERVER ERROR"),
    EMAIL_DUPLICATION(400,"MEMBER-ERR-400","EMAIL DUPLICATED"),


    MYASSET_ERROR_1000(400,"1000","DB 에러"),
    MYASSET_ERROR_1001(400,"1001","자산(Asset) 등록 필요"),
    MYASSET_ERROR_1002(400,"1002","증권사(AssetStock) 먼저 등록"),
    MYASSET_ERROR_1003(400,"1003","주식 종목(StockKind) 먼저 등록"),
    ;

    private int status;
    private String errorCode;
    private String message;

}