package com.idlelife.myasset.controller;


import com.idlelife.myasset.models.dto.*;
import com.idlelife.myasset.models.dto.form.*;
import com.idlelife.myasset.service.StockService;
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
public class StockController {
    @Autowired
    StockService stockService;

    @PostMapping("/stock/total")
    public ResponseEntity<TotalStockAssetDto> getTotalStockAsset(
            @RequestBody AssetSearch dom
    ){
        log.info("call : /stock/total");
        log.info("params : " + dom.toString());
        TotalStockAssetDto result = stockService.getTotalStockAssetDto(dom.getMemberId());
        log.info("result : " + result.toString());

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/stock/acno/reg")
    public ResponseEntity<AssetStockDto> regAssetStock(
            @RequestBody AssetStockForm dom
    ){
        log.info("call : /stock/acno/reg");
        log.info("params : " + dom.toString());
        AssetStockDto result = stockService.regAssetStock(dom);
        log.info("result : " + result.toString());

        return ResponseEntity.ok().body(result);
    }


    @PostMapping("/stock/acno/mod")
    public ResponseEntity<AssetStockDto> modAssetStock(
            @RequestBody AssetStockForm dom
    ){
        log.info("call : /stock/acno/mod");
        log.info("params : " + dom.toString());
        AssetStockDto result = stockService.modAssetStock(dom);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/stock/acno/del")
    public ResponseEntity<AssetStockDto> delAssetStock(
            @RequestBody AssetStockForm dom
    ){
        log.info("call : /stock/acno/del");
        log.info("params : " + dom.getAssetId());
        AssetStockDto result = stockService.delAssetStock(dom.getAssetId());

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/stock/acno/list")
    public ResponseEntity<List<AssetStockDto>> getAssetStockList(
            @RequestBody AssetSearch dom
    ){
        log.info("call : /stock/acno/list");
        log.info("params : " + dom.toString());
        List<AssetStockDto> result = stockService.getAssetStockDtoList(dom);

        return ResponseEntity.ok().body(result);
    }




    @PostMapping("/stock/kind/reg")
    public ResponseEntity<StockKindDto> regStockKind(
            @RequestBody StockKindForm dom
    ){
        log.info("call : /stock/kind/reg");
        log.info("params : " + dom.toString());
        StockKindDto result = stockService.regStockKind(dom);

        return ResponseEntity.ok().body(result);
    }


    @PostMapping("/stock/kind/mod")
    public ResponseEntity<StockKindDto> modStockKind(
            @RequestBody StockKindForm dom
    ){
        log.info("call : /stock/kind/mod");
        log.info("params : " + dom.toString());
        StockKindDto result = stockService.modStockKind(dom);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/stock/kind/del")
    public ResponseEntity<StockKindDto> delStockKind(
            @RequestBody StockKindForm dom
    ){
        log.info("call : /stock/kind/del");
        log.info("params : " + dom.getStockKindId());
        StockKindDto result = stockService.delStockKind(dom.getStockKindId());

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/stock/kind/list")
    public ResponseEntity<List<StockKindDto>> getStockKindList(
            @RequestBody AssetSearch dom
    ){
        log.info("call : /stock/kind/list");
        log.info("params : " + dom.toString());
        List<StockKindDto> result = stockService.getStockKindDtoList(dom);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/stock/kind/code/search")
    public ResponseEntity<List<StockKindCodeDto>> getStockKindCodeSearch(
            @RequestBody StockKindForm dom
    ){
        log.info("call : /stock/kind/code/search");
        log.info("params : " + dom.toString());
        List<StockKindCodeDto> result = stockService.getStockKindCodeDtoList(dom);

        return ResponseEntity.ok().body(result);
    }






    @PostMapping("/stock/trade/reg")
    public ResponseEntity<StockTradeDto> regStockTrade(
            @RequestBody StockTradeForm dom
    ){
        log.info("call : /stock/trade/reg");
        log.info("params : " + dom.toString());
        StockTradeDto result = stockService.regStockTrade(dom);

        return ResponseEntity.ok().body(result);
    }


    @PostMapping("/stock/trade/mod")
    public ResponseEntity<StockTradeDto> modStockTrade(
            @RequestBody StockTradeForm dom
    ){
        log.info("call : /stock/trade/mod");
        log.info("params : " + dom.toString());
        StockTradeDto result = stockService.modStockTrade(dom);

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/stock/trade/del")
    public ResponseEntity<StockTradeDto> delStockTrade(
            @RequestBody StockTradeForm dom
    ){
        log.info("call : /stock/trade/del");
        log.info("params : " + dom.getStockTradeId());
        StockTradeDto result = stockService.delStockTrade(dom.getStockTradeId());

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/stock/trade/history")
    public ResponseEntity<StockTradeHistoryDto> getStockTradeHistory(
            @RequestBody AssetSearch dom
    ){
        log.info("call : /stock/trade/list");
        log.info("params : " + dom.toString());
        StockTradeHistoryDto result = stockService.getStockTradeHistory(dom);

        return ResponseEntity.ok().body(result);
    }

}
