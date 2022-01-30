package com.idlelife.myasset.controller;


import com.idlelife.myasset.models.bank.dto.AssetBankDto;
import com.idlelife.myasset.models.stock.StockSearch;
import com.idlelife.myasset.models.bank.form.AssetBankForm;
import com.idlelife.myasset.service.BankService;
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
public class BankController {
    @Autowired
    BankService bankService;
 
    @PostMapping("/bank/reg")
    public ResponseEntity<AssetBankDto> regAssetBank(
            @RequestBody AssetBankForm dom
    ){
        log.info("call : /bank/reg");
        log.info("params : " + dom.toString());
        AssetBankDto result = bankService.regAssetBank(dom);

        return ResponseEntity.ok().body(result);
    }


    @PostMapping("/bank/mod")
    public ResponseEntity<AssetBankDto> modAssetBank(
            @RequestBody AssetBankForm dom
    ){
        log.info("call : /bank/mod");
        log.info("params : " + dom.toString());
        AssetBankDto result = bankService.modAssetBank(dom);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/bank/del")
    public ResponseEntity<AssetBankDto> delAssetBank(
            @RequestBody AssetBankForm dom
    ){
        log.info("call : /bank/del");
        log.info("params : " + dom.getAssetId());
        AssetBankDto result = bankService.delAssetBank(dom.getAssetId());

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/bank/list")
    public ResponseEntity<List<AssetBankDto>> getAssetList(
            @RequestBody StockSearch dom
    ){
        log.info("call : /bank/list");
        log.info("params : " + dom.toString());
        List<AssetBankDto> result = bankService.getAssetBankDtoList(dom);

        return ResponseEntity.ok().body(result);
    }

}
