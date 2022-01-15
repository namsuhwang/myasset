package com.idlelife.myasset.controller;

import com.idlelife.myasset.models.dto.AssetReDto;
import com.idlelife.myasset.models.dto.ScrapKospiStockDto;
import com.idlelife.myasset.models.dto.ScrapStockKindDto;
import com.idlelife.myasset.service.ScrapStockService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestController
@EnableWebMvc
@RequestMapping(value = "/myasset", produces="application/json;charset=UTF-8")
public class ScrapStockController {

    @Autowired
    ScrapStockService scrapStockService;

    @GetMapping("/scrap/kospi/all")
    public ResponseEntity<List<ScrapKospiStockDto>> getKosPiStockList(HttpServletRequest request) {
        List<ScrapKospiStockDto> result = scrapStockService.getKosPiStockList();

        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/scrap/stockkind")
    public ResponseEntity<ScrapStockKindDto> getStockKind(
            @RequestBody ScrapStockKindDto dom){
        log.info("/scrap/stockkind");
        log.info("kindCode : " + dom.getKindCode());
        ScrapStockKindDto result = scrapStockService.getScrapStockKind(dom.getKindCode());

        log.info("종목정보 : " + result.toString());
        return ResponseEntity.ok().body(result);
    }
}
