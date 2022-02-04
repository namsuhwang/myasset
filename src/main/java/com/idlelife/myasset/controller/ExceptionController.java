package com.idlelife.myasset.controller;

import com.idlelife.myasset.common.exception.MyassetException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.idlelife.myasset.models.common.ErrorCode.AUTHENTICATION_ENTRY_POINT_EXCEPTION;
import static com.idlelife.myasset.models.common.ErrorCode.FORBIDDEN_EXCEPTION;

@RestController
@RequestMapping(value = "/exception")
public class ExceptionController {
	
	
	/**
     * @method 설명 : 401 Error 반환용 컨트롤러
	 */
    @RequestMapping(value = {"/entrypoint"})
    public ResponseEntity<String> entrypointException() {
        throw new MyassetException(AUTHENTICATION_ENTRY_POINT_EXCEPTION);
    }

    /**
     * @method 설명 : 403 Error 반환용 컨트롤러
     */
    @RequestMapping(value = {"/accessdenied"})
    public ResponseEntity<String> accessdeniedException() {    	
        throw new MyassetException(FORBIDDEN_EXCEPTION);
    }
}
