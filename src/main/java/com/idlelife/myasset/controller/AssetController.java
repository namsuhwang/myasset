package com.idlelife.myasset.controller;


import com.idlelife.myasset.common.CommonUtil;
import com.idlelife.myasset.models.asset.AssetSearch;
import com.idlelife.myasset.models.asset.dto.AssetDto;
import com.idlelife.myasset.models.asset.dto.TotalAssetDto;
import com.idlelife.myasset.models.asset.dto.TotalAssetSummaryDto;
import com.idlelife.myasset.models.asset.form.AssetForm;
import com.idlelife.myasset.models.asset.entity.AssetEntity;
import com.idlelife.myasset.service.AssetService;
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
public class AssetController {
    @Autowired
    AssetService assetService;

    @PostMapping("/asset/reg")
    public ResponseEntity<AssetDto> regAsset(
            @RequestBody AssetForm dom
    ){
        log.info("call : /asset/reg");
        log.info("params : " + dom.toString());
        AssetEntity assetEntity = assetService.getAssetEntityFromForm(dom);
        AssetDto result = assetService.regAsset(assetEntity);

        return ResponseEntity.ok().body(result);
    }


    @PostMapping("/asset/mod")
    public ResponseEntity<AssetDto> modAsset(
            @RequestBody AssetForm dom
    ){
        log.info("call : /asset/mod");
        log.info("params : " + dom.toString());
        AssetEntity assetEntity = assetService.getAssetEntityFromForm(dom);
        AssetDto result = assetService.modAsset(assetEntity);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/asset/del")
    public ResponseEntity<AssetDto> delAsset(
            @RequestBody AssetForm dom
    ){
        log.info("call : /asset/del");
        log.info("params : " + dom.getAssetId());
        AssetDto result = assetService.delAsset(dom.getAssetId());

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/asset/list")
    public ResponseEntity<List<AssetDto>> getAssetList(
            @RequestBody AssetSearch dom
    ){
        log.info("call : /asset/list");
        log.info("params : " + dom.toString());
        List<AssetDto> result = assetService.getAssetDtoList();
        log.info("result : " + result.toString());

        return ResponseEntity.ok().body(result);
    }




    @PostMapping("/asset/total/summary")
    public ResponseEntity<TotalAssetSummaryDto> getTotalAssetSummary(
            // @RequestBody AssetForm dom
    ){
        log.info("call 종합현황 요약 : /asset/total/summary ");
        // log.info("params : " + dom.toString());
        TotalAssetSummaryDto result = assetService.getTotalAssetSummary(CommonUtil.getAuthInfo().getMemberId());

        log.info("result : " + result.toString());
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/asset/total/list")
    public ResponseEntity<List<AssetDto>> getTotalAssetList(
            // @RequestBody AssetSearch dom
    ){
        log.info("call 종합현황 자산 목록 : /asset/total/list ");
        // log.info("params : " + dom.toString());
        List<AssetDto> result = assetService.getAssetDtoList();

        log.info("result : " + result.toString());
        return ResponseEntity.ok().body(result);
    }


    @PostMapping("/asset/total")
    public ResponseEntity<TotalAssetDto> getTotalAsset(
            @RequestBody AssetSearch dom
    ){
        log.info("call 종합현황 자산 목록 : /asset/total ");
        log.info("params : " + dom.toString());
        TotalAssetDto result = assetService.getTotalAsset(dom.getMemberId());

        log.info("result : " + result.toString());
        return ResponseEntity.ok().body(result);
    }

}
