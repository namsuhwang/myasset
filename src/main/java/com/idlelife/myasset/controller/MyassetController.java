package com.idlelife.myasset.controller;


import com.idlelife.myasset.models.dto.AssetDto;
import com.idlelife.myasset.models.dto.AssetSearch;
import com.idlelife.myasset.models.dto.form.AssetForm;
import com.idlelife.myasset.models.dto.form.ControllerForm;
import com.idlelife.myasset.service.MyassetService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

//@Api(tags = "[ 나의 자산 ]")
@Slf4j
@RestController
@EnableWebMvc
@RequestMapping(value = "/myasset", produces="application/json;charset=UTF-8")
public class MyassetController {
    @Autowired
    MyassetService myassetService;

    @GetMapping("/test/get")
    public ResponseEntity<String> getAssetTest(){
        String result = myassetService.getAssetTest();

        log.info("Get Call : " + result);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/test/post")
    public ResponseEntity<String> getAssetPostTest(
            @RequestBody ControllerForm param1
    ){
        log.info("111111");
        log.info(param1.toString());
        String result = myassetService.getAssetTest();

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/asset/reg")
    public ResponseEntity<AssetDto> regAsset(
            @RequestBody AssetForm dom
    ){
        log.info("call : /asset/reg");
        log.info("params : " + dom.toString());
        AssetDto result = myassetService.regAsset(dom);

        return ResponseEntity.ok().body(result);
    }


    @PostMapping("/asset/mod")
    public ResponseEntity<AssetDto> modAsset(
            @RequestBody AssetForm dom
    ){
        log.info("call : /asset/mod");
        log.info("params : " + dom.toString());
        AssetDto result = myassetService.modAsset(dom);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/asset/del")
    public ResponseEntity<AssetDto> delAsset(
            @RequestBody AssetForm dom
    ){
        log.info("call : /asset/del");
        log.info("params : " + dom.getAssetId());
        AssetDto result = myassetService.delAsset(dom.getAssetId());

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/asset/list")
    public ResponseEntity<List<AssetDto>> getAssetList(
            @RequestBody AssetSearch dom
    ){
        log.info("call : /asset/list");
        log.info("params : " + dom.toString());
        List<AssetDto> result = myassetService.getAssetDtoList(dom);

        return ResponseEntity.ok().body(result);
    }

}
