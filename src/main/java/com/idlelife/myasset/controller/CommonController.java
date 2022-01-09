package com.idlelife.myasset.controller;


import com.idlelife.myasset.models.dto.*;
import com.idlelife.myasset.models.dto.form.AssetForm;
import com.idlelife.myasset.models.dto.form.CommonCodeForm;
import com.idlelife.myasset.models.dto.form.ControllerForm;
import com.idlelife.myasset.models.entity.AssetEntity;
import com.idlelife.myasset.service.AssetService;
import com.idlelife.myasset.service.CommonService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

//@Api(tags = "[ 나의 자산 ]")
@Slf4j
@RestController
@EnableWebMvc
@RequestMapping(value = "/myasset", produces="application/json;charset=UTF-8")
public class CommonController {
    @Autowired
    CommonService commonService;


    @PostMapping("/common/codelist")
    public ResponseEntity<List<CommonCodeDto>> getCommonCodeDtoList(
            @RequestBody CommonCodeForm dom
    ){
        log.info("call : /common/codelist");
        log.info("params : " + dom.toString());
        List<CommonCodeDto> result = commonService.getCommonCodeDtoList(dom);

        return ResponseEntity.ok().body(result);
    }



}
