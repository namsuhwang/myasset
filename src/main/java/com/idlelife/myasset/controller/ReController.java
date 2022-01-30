package com.idlelife.myasset.controller;


import com.idlelife.myasset.models.re.dto.AssetReDto;
import com.idlelife.myasset.models.re.dto.AssetReRentDto;
import com.idlelife.myasset.models.stock.StockSearch;
import com.idlelife.myasset.models.re.form.AssetReForm;
import com.idlelife.myasset.models.re.form.AssetReRentForm;
import com.idlelife.myasset.service.ReService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.List;

//@Api(tags = "[ 나의 자산 ]")
@Slf4j
@RestController
@EnableWebMvc
@RequestMapping(value = "/myasset", produces="application/json;charset=UTF-8")
public class ReController {
    @Autowired
    ReService reService;
 
    @PostMapping("/re/reg")
    public ResponseEntity<AssetReDto> regAssetRe(
            @RequestBody AssetReForm dom
    ){
        log.info("call : /re/reg");
        log.info("params : " + dom.toString());
        AssetReDto result = reService.regAssetRe(dom);

        return ResponseEntity.ok().body(result);
    }


    @PostMapping("/re/mod")
    public ResponseEntity<AssetReDto> modAssetRe(
            @RequestBody AssetReForm dom
    ){
        log.info("call : /re/mod");
        log.info("params : " + dom.toString());
        AssetReDto result = reService.modAssetRe(dom);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/re/del")
    public ResponseEntity<AssetReDto> delAssetRe(
            @RequestBody AssetReForm dom
    ){
        log.info("call : /re/del");
        log.info("params : " + dom.getAssetId());
        AssetReDto result = reService.delAssetRe(dom.getAssetId());

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/re/list")
    public ResponseEntity<List<AssetReDto>> getAssetList(
            @RequestBody StockSearch dom
    ){
        log.info("call : /re/list");
        log.info("params : " + dom.toString());
        List<AssetReDto> result = reService.getAssetReDtoList(dom);

        return ResponseEntity.ok().body(result);
    }




    @PostMapping("/re/rent/reg")
    public ResponseEntity<AssetReRentDto> regAssetReRent(
            @RequestBody AssetReRentForm dom
    ){
        log.info("call : /re/rent/reg");
        log.info("params : " + dom.toString());
        AssetReRentDto result = reService.regAssetReRent(dom);

        return ResponseEntity.ok().body(result);
    }


    @PostMapping("/re/rent/mod")
    public ResponseEntity<AssetReRentDto> modAssetReRent(
            @RequestBody AssetReRentForm dom
    ){
        log.info("call : /re/rent/mod");
        log.info("params : " + dom.toString());
        AssetReRentDto result = reService.modAssetReRent(dom);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/re/rent/del")
    public ResponseEntity<AssetReRentDto> delAssetReRent(
            @RequestBody AssetReRentForm dom
    ){
        log.info("call : /re/rent/del");
        log.info("params : " + dom.getReRentId());
        AssetReRentDto result = reService.delAssetReRent(dom.getReRentId());

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/re/rent/list")
    public ResponseEntity<List<AssetReRentDto>> getAssetRentList(
            @RequestBody StockSearch dom
    ){
        log.info("call : /re/rent/list");
        log.info("params : " + dom.toString());
        List<AssetReRentDto> result = reService.getAssetReRentDtoList(dom);

        return ResponseEntity.ok().body(result);
    }

}
