package com.idlelife.myasset.service;

import com.idlelife.myasset.common.exception.MyassetException;
import com.idlelife.myasset.models.dto.*;
import com.idlelife.myasset.models.dto.form.*;
import com.idlelife.myasset.models.entity.*;
import com.idlelife.myasset.repository.AssetStockMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.idlelife.myasset.models.common.ErrorCode.*;

@Log4j2
@Service
@Transactional
public class StockService {
    @Autowired
    AssetStockMapper assetStockMapper;

    @Autowired
    AssetService assetService;

    @Autowired
    CommonService commonService;

    public AssetStockDto getAssetStockDto(Long assetId){
        return assetStockMapper.selectAssetStockDto(assetId);
    }

    public AssetStockEntity getAssetStock(Long assetId){
        return assetStockMapper.selectAssetStock(assetId);
    }

    public AssetStockDto regAssetStock(AssetStockForm form){
        log.info("Asset 등록");
        AssetForm assetForm = new AssetForm();
        assetForm.setAssetName(form.getAssetName());
        assetForm.setAssetType("STOCK");
        assetForm.setMemberId(form.getMemberId());
        assetForm.setAbleAmt(form.getAbleAmt());
        assetForm.setEvalAmt(form.getAbleAmt() - form.getLoanBalAmt());  // 처음 자산 등록시에는 평가금액 0원. 추후 세부 자산 등록시 업데이트해야 함.
        AssetEntity assetEntity = assetService.getAssetEntityFromForm(assetForm);
        AssetDto assetDto = assetService.regAsset(assetEntity);
        if(assetDto == null){
            throw new MyassetException("DB 에러 : 자산 등록 실패", MYASSET_ERROR_1000);
        }

        log.info("AssetStock 등록");
        form.setAssetId(assetDto.getAssetId());
        AssetStockEntity assetStockEntity = getAssetStockEntityFromForm(form);
        int cnt = assetStockMapper.insertAssetStock(assetStockEntity);
        if(cnt < 1){
            throw new MyassetException("DB 에러 : 증권사 등록 실패", MYASSET_ERROR_1000);
        }
        AssetStockDto assetStockDto = assetStockMapper.selectAssetStockDto(assetStockEntity.getAssetId());
        return assetStockDto;
    }

    public AssetStockDto modAssetStock(AssetStockForm form){
        log.info("Asset 수정");
        log.info("기존 Asset 조회");
        AssetEntity assetEntity = assetService.getAsset(form.getAssetId());
        assetEntity.setAssetName(form.getAssetName());
        assetEntity.setEvalAmt(form.getAbleAmt() - form.getLoanBalAmt());
        AssetDto assetDto = assetService.modAsset(assetEntity);
        if(assetDto == null){
            throw new MyassetException("DB 에러 : 자산 수정 실패", MYASSET_ERROR_1000);
        }

        AssetStockEntity assetStockEntity = getAssetStockEntityFromForm(form);
        int cnt = assetStockMapper.updateAssetStock(assetStockEntity);
        if(cnt < 1){
            throw new RuntimeException();
        }

        AssetStockDto assetStockDto = assetStockMapper.selectAssetStockDto(assetStockEntity.getAssetId());
        return assetStockDto;
    }

    public AssetStockDto delAssetStock(Long assetId){
        int cnt = assetStockMapper.deleteAssetStock(assetId);
        if(cnt < 1){
            throw new MyassetException("DB 에러 : 증권사 삭제 실패", MYASSET_ERROR_1000);
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
        assetStockEntity.setMemberId(form.getMemberId());
        assetStockEntity.setOrgCd(form.getOrgCd());
        assetStockEntity.setOrgName(form.getOrgName());
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
        log.info("주식 종목(StockKind) 등록");
        if(form.getAssetId() == null){
            log.error("Asset 먼저 등록");
            throw new MyassetException(MYASSET_ERROR_1001);
        }

        AssetStockEntity assetStockEntity = assetStockMapper.selectAssetStock(form.getAssetId());
        if(assetStockEntity == null){
            log.error("증권사(AssetStock) 먼저 등록");
            throw new MyassetException(MYASSET_ERROR_1002);
        }

        if(form.getMemberId() == null){
            form.setMemberId(assetStockEntity.getMemberId());
        }

        log.info("자산 정보 업데이트");
        AssetEntity assetEntity = assetService.getAsset(form.getAssetId());
        long evalAmt = assetEntity.getEvalAmt() == null ? form.getCurTotPrice() :  assetEntity.getEvalAmt() + form.getCurTotPrice();
        assetEntity.setEvalAmt(evalAmt);
        assetService.modAsset(assetEntity);

        log.info("주식 종목 코드 등록/수정(StockKindCode)");
        setStockKindCode(form.getStockKindCd(), form.getStockKindName());

        log.info("자산 종목(StockKind) 등록");
        StockKindEntity stockKindEntity = getStockKindEntityFromForm(form);
        long pnlAmt = stockKindEntity.getCurTotPrice() - stockKindEntity.getBuyTotPrice();
        double pnlRate = Math.round((double)pnlAmt / (double)stockKindEntity.getBuyTotPrice() * 10000.0) / 100.0;
        stockKindEntity.setPnlAmt(pnlAmt);
        stockKindEntity.setPnlRate(pnlRate);
        int cnt = assetStockMapper.insertStockKind(stockKindEntity);
        if(cnt < 1){
            throw new MyassetException("DB 에러 : 주식 종목 등록 실패", MYASSET_ERROR_1000);
        }
        StockKindDto stockKindDto = assetStockMapper.selectStockKindDto(stockKindEntity.getStockKindId());
        return stockKindDto;
    }


    public StockKindDto modStockKind(StockKindForm form){
        log.info("주식 종목(StockKind) 수정");
        if(form.getAssetId() == null){
            log.error("Asset 먼저 등록");
            throw new MyassetException(MYASSET_ERROR_1001);
        }

        // 증권사(AssetStock) 는 예수금, 대출잔액관 관리하므로 종목(StockKind) 관리에서는 업데이트 하지 않음
        AssetStockEntity assetStockEntity = assetStockMapper.selectAssetStock(form.getAssetId());
        if(assetStockEntity == null){
            log.error("증권사(AssetStock) 먼저 등록");
            throw new MyassetException(MYASSET_ERROR_1002);
        }

        log.info("자산 정보 업데이트");
        AssetEntity assetEntity = assetService.getAsset(form.getAssetId());
        long evalAmt = assetEntity.getEvalAmt() == null ? form.getCurTotPrice() :  assetEntity.getEvalAmt() + form.getCurTotPrice();
        assetEntity.setEvalAmt(evalAmt);
        assetService.modAsset(assetEntity);

        log.info("주식 종목 코드 등록/수정(StockKindCode)");
        setStockKindCode(form.getStockKindCd(), form.getStockKindName());

        log.info("주식 종목(StockKind) 수정");
        StockKindEntity stockKindEntity = getStockKindEntityFromForm(form);

        int cnt = assetStockMapper.updateStockKind(stockKindEntity);
        if(cnt < 1){
            throw new MyassetException("DB 에러 : 주식 종목 수정 실패", MYASSET_ERROR_1000);
        }

        StockKindDto stockKindDto = assetStockMapper.selectStockKindDto(stockKindEntity.getStockKindId());
        return stockKindDto;
    }

    public StockKindDto delStockKind(Long stockKindId){
        int cnt = assetStockMapper.deleteStockKind(stockKindId);
        if(cnt < 1){
            throw new MyassetException("DB 에러 : 주식 종목 삭제 실패", MYASSET_ERROR_1000);
        }
        return assetStockMapper.selectStockKindDto(stockKindId);
    }

    public List<StockKindDto> getStockKindDtoList(AssetSearch dom){
        List<StockKindDto> list = assetStockMapper.selectStockKindDtoList(dom);
        return list;
    }

    public StockKindDto updateStockKindCurrentStatus(StockKindForm form){
        log.info("주식 종목 현재가 수정");
        if(form.getAssetId() == null){
            log.error("Asset 먼저 등록");
            throw new MyassetException(MYASSET_ERROR_1001);
        }

        // 증권사(AssetStock) 는 예수금, 대출잔액관 관리하므로 종목(StockKind) 관리에서는 업데이트 하지 않음
        AssetStockEntity assetStockEntity = assetStockMapper.selectAssetStock(form.getAssetId());
        if(assetStockEntity == null){
            log.error("증권사(AssetStock) 먼저 등록");
            throw new MyassetException(MYASSET_ERROR_1002);
        }

        log.info("주식 종목(StockKind) 수정");
        StockKindEntity stockKindEntity = getStockKindEntityFromForm(form);

        int cnt = assetStockMapper.updateStockKind(stockKindEntity);
        if(cnt < 1){
            throw new MyassetException("DB 에러 : 주식 종목 수정 실패", MYASSET_ERROR_1000);
        }

        StockKindDto stockKindDto = assetStockMapper.selectStockKindDto(stockKindEntity.getStockKindId());
        return stockKindDto;
    }




    private StockKindEntity getStockKindEntityFromForm(StockKindForm form){
        StockKindEntity stockKindEntity = new StockKindEntity();
        stockKindEntity.setStockKindId(form.getStockKindId() == null ? assetStockMapper.createStockKindId() : form.getStockKindId());
        stockKindEntity.setAssetId(form.getAssetId());
        stockKindEntity.setMemberId(form.getMemberId());
        stockKindEntity.setStockKindCd(form.getStockKindCd());
        stockKindEntity.setStockKindName(form.getStockKindName());
        stockKindEntity.setQuantity(form.getQuantity());
        stockKindEntity.setBuyAvgPrice(form.getBuyAvgPrice());
        stockKindEntity.setBuyTotPrice(form.getBuyTotPrice());
        stockKindEntity.setCurUnitPrice(form.getCurUnitPrice());
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
        log.info("주식 거래 등록");
        StockKindEntity stockKindEntity = assetStockMapper.selectStockKind(form.getStockKindId());
        if(stockKindEntity == null){
            log.error("주식 종목(StockKind) 먼저 등록");
            throw new MyassetException(MYASSET_ERROR_1003);
        }

        log.info("주식 종목 정보 업데이트");
        StockTradeEntity stockTradeEntity = getStockTradeEntityFromForm(form);
        log.info("주식 평단가 계산");
        long buyTotPrice = 0;
        long avgPrice = 0;
        log.info("보유수량, 평단가 등 계산");
        if(form.getTradeType().equals("BUY")) {
            log.info("매수");
            stockKindEntity.setQuantity(stockKindEntity.getQuantity() + stockTradeEntity.getTradeQuantity());
            buyTotPrice = stockKindEntity.getBuyTotPrice() + stockTradeEntity.getTradeAmt();
            avgPrice = buyTotPrice / stockKindEntity.getQuantity();
        }else{
            log.info("매도");
            stockKindEntity.setQuantity(stockKindEntity.getQuantity() - stockTradeEntity.getTradeQuantity());
            buyTotPrice = stockKindEntity.getBuyTotPrice() - stockTradeEntity.getTradeAmt();
            avgPrice = buyTotPrice / stockKindEntity.getQuantity();
        }
        stockKindEntity.setBuyTotPrice(buyTotPrice);
        stockKindEntity.setBuyAvgPrice(avgPrice);
        assetStockMapper.updateStockKind(stockKindEntity);

        log.info("주식 거래내역 등록");
        StockTradeEntity stockTradeEntityOld = assetStockMapper.selectLastStockTrade(stockKindEntity.getStockKindId());
        stockTradeEntity.setBefQuantity(stockTradeEntityOld.getAftQuantity());
        stockTradeEntity.setBefBuyAvgPrice(stockTradeEntityOld.getAftBuyAvgPrice());
        stockTradeEntity.setBefBuyTotPrice(stockTradeEntityOld.getAftBuyTotPrice());
        stockTradeEntity.setAftQuantity(stockKindEntity.getQuantity());
        stockTradeEntity.setAftBuyAvgPrice(stockKindEntity.getBuyAvgPrice());
        stockTradeEntity.setAftBuyTotPrice(stockKindEntity.getBuyTotPrice());
        int cnt = assetStockMapper.insertStockTrade(stockTradeEntity);
        if(cnt < 1){
            throw new MyassetException("DB 에러 : 주식 거래 내역 등록 실패", MYASSET_ERROR_1000);
        }

        return assetStockMapper.selectStockTradeDto(stockTradeEntity.getStockTradeId());
    }

    public StockTradeDto modStockTrade(StockTradeForm form){
        StockTradeEntity stockTradeEntity = getStockTradeEntityFromForm(form);

        int cnt = assetStockMapper.updateStockTrade(stockTradeEntity);
        if(cnt < 1){
            throw new MyassetException("DB 에러 : 주식 거래 내역 조회 실패", MYASSET_ERROR_1000);
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
        stockTradeEntity.setStockTradeId(form.getStockTradeId() == null ? assetStockMapper.createStockTradeId() : form.getStockTradeId());
        stockTradeEntity.setTradeType(form.getTradeType());
        stockTradeEntity.setStockKindId(form.getStockTradeId());
        stockTradeEntity.setTradeDatetime(form.getTradeDatetime());
        stockTradeEntity.setTradeTypeName(form.getTradeTypeName());
        stockTradeEntity.setTradeQuantity(form.getTradeQuantity());
        stockTradeEntity.setTradeUnitPrice(form.getTradeUnitPrice());
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



    private void setStockKindCode(String code, String kindName){
        StockKindCodeDto stockKindCodeDto;
        stockKindCodeDto = commonService.getStockKindCode(code);
        if(stockKindCodeDto == null){
            stockKindCodeDto = new StockKindCodeDto();
            stockKindCodeDto.setCode(code);
            stockKindCodeDto.setKindName(kindName);
            commonService.insertStockKindCode(stockKindCodeDto);
        }else if(!stockKindCodeDto.getKindName().equals(kindName)){
            stockKindCodeDto.setKindName(kindName);
            commonService.updateStockKindCode(stockKindCodeDto);
        }

    }

}
