package com.idlelife.myasset.service;

import com.idlelife.myasset.component.stock.JsoupComponent;
import com.idlelife.myasset.models.stock.dto.ScrapKospiStockDto;
import com.idlelife.myasset.models.stock.dto.ScrapStockKindDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ScrapStockService {
    private final JsoupComponent jsoupComponent;

    public ScrapStockKindDto getScrapStockKind(String kindCode){
        return jsoupComponent.getScrapStockKindDto(kindCode);
    }

    public List<ScrapKospiStockDto> getKosPiStockList() {
        return jsoupComponent.getKosPiStockList();
    }
}
