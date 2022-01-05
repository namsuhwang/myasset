package com.idlelife.myasset.service;

import com.idlelife.myasset.models.dto.*;
import com.idlelife.myasset.models.dto.form.*;
import com.idlelife.myasset.models.entity.*;
import com.idlelife.myasset.repository.AssetStockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {
    @Autowired
    AssetStockMapper assetStockMapper;

    @Autowired
    MyassetService assetService;
 
    public AssetStockDto getAssetStockDto(Long assetId){
        return assetStockMapper.selectAssetStockDto(assetId);
    }

    public AssetStockEntity getAssetStock(Long assetId){
        return assetStockMapper.selectAssetStock(assetId);
    }

    public AssetStockDto regAssetStock(AssetStockForm form){
        AssetForm assetForm = new AssetForm();
        assetForm.setAssetName(form.getAssetName());
        assetForm.setAssetType("STOCK");
        assetForm.setMemberId(1L);
        assetForm.setEvalAmt(0L);  // 처음 자산 등록시에는 평가금액 0원. 추후 세부 자산 등록시 업데이트해야 함.
        AssetDto assetDto = assetService.regAsset(assetForm);
        if(assetDto == null){
            throw new RuntimeException();
        }

        form.setAssetId(assetDto.getAssetId());
        AssetStockEntity assetStockEntity = getAssetStockEntityFromForm(form);
        int cnt = assetStockMapper.insertAssetStock(assetStockEntity);
        if(cnt < 1){
            throw new RuntimeException();
        }
        return assetStockMapper.selectAssetStockDto(assetStockEntity.getAssetId());
    }

    public AssetStockDto modAssetStock(AssetStockForm form){
        AssetStockEntity assetStockEntity = getAssetStockEntityFromForm(form);

        int cnt = assetStockMapper.updateAssetStock(assetStockEntity);
        if(cnt < 1){
            throw new RuntimeException();
        }
        return assetStockMapper.selectAssetStockDto(assetStockEntity.getAssetId());
    }

    public AssetStockDto delAssetStock(Long assetId){
        int cnt = assetStockMapper.deleteAssetStock(assetId);
        if(cnt < 1){
            throw new RuntimeException();
        }
        return assetStockMapper.selectAssetStockDto(assetId);
    }

    public List<AssetStockDto> getAssetStockDtoList(AssetSearch dom){
        List<AssetStockDto> list = assetStockMapper.selectAssetStockDtoList(dom);
        return list;
    }

    private AssetStockEntity getAssetStockEntityFromForm(AssetStockForm form){
        AssetStockEntity assetStockEntity = new AssetStockEntity();
        assetStockEntity.setAssetId(form.getAssetId());
        assetStockEntity.setSecurityCd(form.getSecurityCd());
        assetStockEntity.setSecurityName(form.getSecurityName());
        assetStockEntity.setStockAcno(form.getStockAcno());
        assetStockEntity.setAbleAmt(form.getAbleAmt());
        assetStockEntity.setLoanBalAmt(form.getLoanBalAmt());
        assetStockEntity.setLoanRate(form.getLoanRate());
        assetStockEntity.setDeleteYn("N");
        return assetStockEntity;
    }






    public StockKindDto getStockKindDto(Long stockKindId){
        return assetStockMapper.selectStockKindDto(stockKindId);
    }

    public StockKindEntity getStockKind(Long stockKindId){
        return assetStockMapper.selectStockKind(stockKindId);
    }

    public StockKindDto regStockKind(StockKindForm form){
        StockKindEntity stockKindEntity = getStockKindEntityFromForm(form);

        int cnt = assetStockMapper.insertStockKind(stockKindEntity);
        if(cnt < 1){
            throw new RuntimeException();
        }
        return assetStockMapper.selectStockKindDto(stockKindEntity.getStockKindId());
    }

    public StockKindDto modStockKind(StockKindForm form){
        StockKindEntity stockKindEntity = getStockKindEntityFromForm(form);

        int cnt = assetStockMapper.updateStockKind(stockKindEntity);
        if(cnt < 1){
            throw new RuntimeException();
        }
        return assetStockMapper.selectStockKindDto(stockKindEntity.getStockKindId());
    }
    public StockKindDto delStockKind(Long stockKindId){
        int cnt = assetStockMapper.deleteStockKind(stockKindId);
        if(cnt < 1){
            throw new RuntimeException();
        }
        return assetStockMapper.selectStockKindDto(stockKindId);
    }

    public List<StockKindDto> getStockKindDtoList(AssetSearch dom){
        List<StockKindDto> list = assetStockMapper.selectStockKindDtoList(dom);
        return list;
    }

    private StockKindEntity getStockKindEntityFromForm(StockKindForm form){
        StockKindEntity stockKindEntity = new StockKindEntity();
        stockKindEntity.setStockKindId(form.getStockKindId());
        stockKindEntity.setAssetId(form.getAssetId());
        stockKindEntity.setStockKindCd(form.getStockKindCd());
        stockKindEntity.setStockKindName(form.getStockKindName());
        stockKindEntity.setStockAcno(form.getStockAcno());
        stockKindEntity.setQuantity(form.getQuantity());
        stockKindEntity.setBuyAvgPrice(form.getBuyAvgPrice());
        stockKindEntity.setBuyTotPrice(form.getBuyTotPrice());
        stockKindEntity.setCurPrice(form.getCurPrice());
        stockKindEntity.setCurTotPrice(form.getCurTotPrice());
        stockKindEntity.setPnlRate(form.getPnlRate());
        stockKindEntity.setPnlAmt(form.getPnlAmt());
        stockKindEntity.setDeleteYn("N");
        return stockKindEntity;
    }










    public StockTradeDto getStockTradeDto(Long stockTradeId){
        return assetStockMapper.selectStockTradeDto(stockTradeId);
    }

    public StockTradeEntity getStockTrade(Long stockTradeId){
        return assetStockMapper.selectStockTrade(stockTradeId);
    }

    public StockTradeDto regStockTrade(StockTradeForm form){
        StockTradeEntity stockTradeEntity = getStockTradeEntityFromForm(form);

        int cnt = assetStockMapper.insertStockTrade(stockTradeEntity);
        if(cnt < 1){
            throw new RuntimeException();
        }
        return assetStockMapper.selectStockTradeDto(stockTradeEntity.getStockTradeId());
    }

    public StockTradeDto modStockTrade(StockTradeForm form){
        StockTradeEntity stockTradeEntity = getStockTradeEntityFromForm(form);

        int cnt = assetStockMapper.updateStockTrade(stockTradeEntity);
        if(cnt < 1){
            throw new RuntimeException();
        }
        return assetStockMapper.selectStockTradeDto(stockTradeEntity.getStockTradeId());
    }
    public StockTradeDto delStockTrade(Long stockTradeId){
        int cnt = assetStockMapper.deleteStockTrade(stockTradeId);
        if(cnt < 1){
            throw new RuntimeException();
        }
        return assetStockMapper.selectStockTradeDto(stockTradeId);
    }

    public List<StockTradeDto> getStockTradeDtoList(AssetSearch dom){
        List<StockTradeDto> list = assetStockMapper.selectStockTradeDtoList(dom);
        return list;
    }

    private StockTradeEntity getStockTradeEntityFromForm(StockTradeForm form){
        StockTradeEntity stockTradeEntity = new StockTradeEntity();
        stockTradeEntity.setStockTradeId(form.getStockTradeId());
        stockTradeEntity.setTradeType(form.getTradeType());
        stockTradeEntity.setStockKindId(form.getStockTradeId());
        stockTradeEntity.setTradeDatetime(form.getTradeDatetime());
        stockTradeEntity.setTradeTypeName(form.getTradeTypeName());
        stockTradeEntity.setTradeCnt(form.getTradeCnt());
        stockTradeEntity.setTradePrice(form.getTradePrice());
        stockTradeEntity.setTradeAmt(form.getTradeAmt());
        stockTradeEntity.setBefQuantity(form.getBefQuantity());
        stockTradeEntity.setAftQuantity(form.getAftQuantity());
        stockTradeEntity.setBefBuyAvgPrice(form.getBefBuyAvgPrice());
        stockTradeEntity.setBefBuyTotPrice(form.getBefBuyTotPrice());
        stockTradeEntity.setAftBuyAvgPrice(form.getAftBuyAvgPrice());
        stockTradeEntity.setAftBuyTotPrice(form.getAftBuyTotPrice());
        stockTradeEntity.setPnlRate(form.getPnlRate());
        stockTradeEntity.setPnlAmt(form.getPnlAmt());
        stockTradeEntity.setDeleteYn("N");
        return stockTradeEntity;
    }
}
