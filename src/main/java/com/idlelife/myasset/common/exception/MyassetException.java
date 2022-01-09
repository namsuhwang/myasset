package com.idlelife.myasset.common.exception;

import com.idlelife.myasset.models.common.ErrorCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class MyassetException extends RuntimeException{
    private ErrorCode errorCode;

    public MyassetException(String message, ErrorCode errorCode){
        super(message);
        this.errorCode = errorCode;
    }

    public MyassetException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
