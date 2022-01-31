package com.idlelife.myasset.models.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    NOT_FOUND(404,"COMMON-ERR-404","PAGE NOT FOUND"),
    UNAUTHORIZED_EXCEPTION(400,"0001","인증 실패"),
    UNMATCHED_AUTH_INFO_EXCEPTION(400,"0002","이메일 또는 비밀번호 확인 실패"),
    INTER_SERVER_ERROR(500,"COMMON-ERR-500","INTER SERVER ERROR"),
    EMAIL_DUPLICATION(400,"MEMBER-ERR-400","EMAIL DUPLICATED"),


    MYASSET_ERROR_1000(400,"1000","DB 에러"),
    MYASSET_ERROR_1001(400,"1001","자산 등록이 필요합니다."),
    MYASSET_ERROR_1002(400,"1002","증권사를 먼저 등록하십시오."),
    MYASSET_ERROR_1003(400,"1003","주식 종목을 먼저 등록하십시오."),
    MYASSET_ERROR_1004(400,"1004","거래내역 등록 필수입력 항목(거래종류, 종목코드, 거래수량, 거래단가)을 확인하십시오."),
    MYASSET_ERROR_1005(400,"1005","매도수량이 보유수량보다 많습니다."),
    MYASSET_ERROR_1006(400,"1006","이메일주소 확인이 필요합니다."),
    MYASSET_ERROR_1007(400,"1007","다른 사람이 사용중인 이메일주소입니다."),
    ;

    private int status;
    private String errorCode;
    private String message;

}